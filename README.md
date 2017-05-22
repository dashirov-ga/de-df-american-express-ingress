# Data Engineering Data Feed: American Express Revenue (Ingress)
GroupId: **ly.generalassemb.de.datafeeds**
ArtifactId: **df-american-express-ingress**

Handles Revenue on transactions initiated with PayPal Braintree using American Express Credit Cards.

# Configuration 
## de-df-american-express-ingress.properties
```
sink.redshift.jdbc.url = jdbc:redshift://<host>:<port>/<dbname>
sink.redshift.jdbc.usr = <dbuser>
sink.redshift.jdbc.pwd = <dbpass>
sink.redshift.jdbc.credentials = <aws_s3_cedentials_to_user_for_redshift_copy>

sink.s3.bucket.name = <dedicated_s3_bucket_with_versioning_enabled>
sink.s3.credentials.accessKey = <aws access key>
sink.s3.credentials.secretKey = <aws secret key>

sink.postgres.jdbc.url = jdbc:postgresql://<host>:<port>/<dbname>
sink.postgres.jdbc.usr = <dbuser>
sink.postgres.jdbc.pwd = <dbpass>
sink.postgres.jdbc.schema = <dbschema>

monitoring.snowplow.url         = <snowplow host>
monitoring.snowplow.application = df-american-express-ingress
monitoring.snowplow.namespace   = ga.co

source.amex.sftp.host = <hostname|ipaddress>
source.amex.sftp.port = <ssh port>
source.amex.sftp.user = <username>
source.amex.sftp.directory = <incoming data directory>
source.amex.sftp.private_key =\
  -----BEGIN RSA PRIVATE KEY-----\
  <openssh 4096 bit RSA private key : multiline>
  -----END RSA PRIVATE KEY-----
source.amex.sftp.public_key=\
  ssh-rsa <openssh 4096 bit RSA public key : multiline>




```
## Command Line Options
```$xslt
       
-c         <path_to_custom_config_file>
           default: config file location strategy chain: 
           1. look for 'df-american-express-ingress.properties' in the same directory where jar is
           2. look for 'df-american-express-ingress.properties' in the classpath (incl. jar contents)
           
-x         <skip_processing_step:postgres,clean-local,clean-remote,s3,redshift>
           default: skip no steps

E.G.
-x postgres,clean-local,clean-remote,s3,redshift -c /tmp/df-american-express-ingress.properties


```
## Future
At some point we may want to implement a multi-threaded process, similar to df-paypal-braintree. Examples can be found here: https://bitbucket.org/finelean/sftpconnectionpool

## Redshift Tables (Preliminary scratch)
Compression - TBD
Data Distribution - TBD
Data Sort Order - TBD

```derby
 CREATE TABLE IF NOT EXISTS scratch.american_express_revenue_activity_summary
				(
				amex_payee_number NUMERIC NOT NULL,
				payment_year NUMERIC NOT NULL,
				payment_number VARCHAR(8) NOT NULL,
				record_type INT NOT NULL,
				detail_record_type INT NOT NULL,
				payment_date DATE NOT NULL,
				payment_amount DECIMAL NOT NULL,
				debit_balance_amount DECIMAL NOT NULL,
				aba_bank_number NUMERIC,
				payee_direct_deposit_acount_number VARCHAR(17)
				);
COMMENT ON TABLE scratch.american_express_revenue_activity_summary IS 'EPTRN Data File Summary';

CREATE TABLE IF NOT EXISTS scratch.american_express_revenue_activity_adjustment_detail
				(
				amex_payee_number NUMERIC NOT NULL,
				amex_se_number NUMERIC NOT NULL,
				payment_year NUMERIC NOT NULL,
				payment_number VARCHAR(8) NOT NULL,
				record_type INT NOT NULL,
				detail_record_type INT NOT NULL,
				amex_process_date DATE NOT NULL,
				adjustment_number NUMERIC NOT NULL,
				adjustment_amount DECIMAL NOT NULL,
				discount_amount DECIMAL NOT NULL,
				service_fee_amount DECIMAL NOT NULL,
				net_adjustment_amount DECIMAL NOT NULL,
				discount_rate DECIMAL DEFAULT 0.00000 NOT NULL,
				service_fee_rate DECIMAL DEFAULT 0.00000 NOT NULL,
				card_member_number VARCHAR(17) NOT NULL,
				adjustment_reason VARCHAR(280)
				);
COMMENT ON TABLE scratch.american_express_revenue_activity_adjustment_detail IS 'EPTRN Adjustment Detail';

CREATE TABLE IF NOT EXISTS scratch.american_express_revenue_activity_record_of_charge_detail
				(
				amex_payee_number NUMERIC NOT NULL,
				amex_se_number NUMERIC NOT NULL,
				se_unit_number VARCHAR(10),
				payment_year NUMERIC NOT NULL,
				payment_number VARCHAR(8) NOT NULL,
				record_type INT NOT NULL,
				detail_record_type INT NOT NULL,
				se_business_date DATE NOT NULL,
				amex_process_date DATE NOT NULL,
				soc_invoice_number NUMERIC NOT NULL,
				soc_amount DECIMAL NOT NULL,
				roc_amount DECIMAL NOT NULL,
				card_member_number VARCHAR(17),
				card_member_reference_number VARCHAR(11),
				transaction_Date DATE NOT NULL,
				invoice_reference_number VARCHAR(30),
				non_compliant_indicator VARCHAR(1),
				non_compliant_error_code_1 VARCHAR(4),
				non_compliant_error_code_2 VARCHAR(4),
				non_compliant_error_code_3 VARCHAR(4),
				non_compliant_error_code_4 VARCHAR(4),
				non_swiped_indicator VARCHAR(1),
				card_member_number_extended VARCHAR(19)
				);
COMMENT ON TABLE scratch.american_express_revenue_activity_record_of_charge_detail IS 'EPTRN ROC Detail';				

CREATE TABLE scratch.american_express_revenue_activity_summary_of_charge_detail
				(
				amex_payee_number NUMERIC NOT NULL,
				amex_se_number NUMERIC NOT NULL,
				se_unit_number VARCHAR(10),
				payment_year NUMERIC NOT NULL,
				payment_number VARCHAR(8) NOT NULL,
				record_type INT NOT NULL,
				detail_record_type INT NOT NULL,
				se_business_date DATE NOT NULL,
				amex_process_date DATE NOT NULL,
				soc_invoice_number NUMERIC NOT NULL,
				soc_amount DECIMAL NOT NULL,
				discount_amount DECIMAL NOT NULL,
				service_fee_amount DECIMAL NOT NULL,
				net_soc_amount DECIMAL NOT NULL,
				discount_rate DECIMAL DEFAULT 0.00000 NOT NULL,
				service_fee_rate DECIMAL DEFAULT 0.00000 NOT NULL,
				amex_gross_amount DECIMAL NOT NULL,
				amex_roc_count INT NOT NULL ,
				tracking_id NUMERIC NOT NULL,
				cpc_indecator BOOLEAN,
				amex_roc_count_poa INTEGER NOT NULL
			    );
COMMENT ON TABLE scratch.american_express_revenue_activity_summary_of_charge_detail IS 'EPTRN SOC Detail';


CREATE TABLE scratch.american_express_revenue_activity_chargeback_detail
(
     data_file_record_type VARCHAR(1) NOT NULL,
     service_establishment_number NUMERIC NOT NULL,
     card_member_account_number NUMERIC NOT NULL,
     current_case_number VARCHAR(11),
     previous_case_number VARCHAR(11) ,
     resolution VARCHAR(1) NOT NULL,
     adjustment_date DATE NOT NULL,
     charge_date DATE NOT NULL,
     case_type VARCHAR(6) NOT NULL,
     location_number VARCHAR(15),
     chargeback_reason_code VARCHAR(3) NOT NULL,
     chargeback_amount DECIMAL NOT NULL,
     chargeback_adjustment_number VARCHAR(6),
     chargeback_resolution_adjustment_number VARCHAR(6),
     chargeback_reference_code VARCHAR(12), -- this is core tx id
     billed_amount DECIMAL,
     soc_amount DECIMAL,
     soc_invoice_number VARCHAR(6),
     roc_invoice_number VARCHAR(6),
     foreign_amount DECIMAL,
     foreign_currency VARCHAR(3),
     support_to_follow VARCHAR(1),
     card_member_name_1 VARCHAR(30),
     card_member_name_2 VARCHAR(30),
     card_member_address_1 VARCHAR(30),
     card_member_address_2 VARCHAR(30),
     card_member_city_state VARCHAR(30),
     card_member_zip VARCHAR(9),
     card_member_first_name_1 VARCHAR(12),
     card_member_middle_name_1 VARCHAR(12),
     card_member_last_name_1 VARCHAR(20),
     card_member_original_account_number VARCHAR(15)
);

COMMENT ON TABLE scratch.american_express_revenue_activity_chargeback_detail IS 'CBNOT Detail';


```
