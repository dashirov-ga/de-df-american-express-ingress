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

DATA is destributed by payment number and where applicable, sorted by either date or charge reference number.


```redshift

CREATE TABLE IF NOT EXISTS
  scratch.american_express_eptrn_payment (
  AMEX_PAYEE_NUMBER    NUMERIC(18, 0) ENCODE ZSTD,
  PAYMENT_YEAR         NUMERIC(4, 0) ENCODE ZSTD,
  PAYMENT_NUMBER       VARCHAR(8) PRIMARY KEY DISTKEY ENCODE ZSTD,
  RECORD_TYPE          VARCHAR(1) ENCODE RUNLENGTH,
  DETAIL_RECORD_TYPE   VARCHAR(2) ENCODE RUNLENGTH,
  PAYMENT_DATE         DATE SORTKEY ENCODE ZSTD,
  PAYMENT_AMOUNT       NUMERIC(18, 2) ENCODE ZSTD,
  DEBIT_BALANCE_AMOUNT NUMERIC(18, 2) ENCODE RUNLENGTH,
  ABA_BANK_NUMBER      VARCHAR(9) ENCODE RUNLENGTH,
  SE_DDA_NUMBER        VARCHAR(17) ENCODE RUNLENGTH
)
  DISTSTYLE KEY;

CREATE TABLE IF NOT EXISTS 
  scratch.american_express_eptrn_summary_of_charges (
  AMEX_PAYEE_NUMBER          NUMERIC(18, 0) ENCODE ZSTD,
  AMEX_SE_NUMBER             NUMERIC(18, 0) ENCODE ZSTD,
  SE_UNIT_NUMBER             VARCHAR(10) ENCODE ZSTD,
  PAYMENT_YEAR               NUMERIC(4, 0) ENCODE ZSTD,
  PAYMENT_NUMBER             VARCHAR(8) DISTKEY ENCODE ZSTD,
  RECORD_TYPE                VARCHAR(1) ENCODE RUNLENGTH,
  DETAIL_RECORD_TYPE         VARCHAR(2) ENCODE RUNLENGTH,
  SE_BUSINESS_DATE           DATE ENCODE ZSTD,
  AMEX_PROCESS_DATE          DATE ENCODE ZSTD,
  SOC_INVOICE_NUMBER         NUMERIC(6, 0) ENCODE ZSTD,
  SOC_AMOUNT                 NUMERIC(18, 2) ENCODE ZSTD,
  DISCOUNT_AMOUNT            NUMERIC(18, 2) ENCODE ZSTD,
  SERVICE_FEE_AMOUNT         NUMERIC(18, 2) ENCODE ZSTD,
  NET_SOC_AMOUNT             NUMERIC(18, 2) ENCODE ZSTD,
  DISCOUNT_RATE              NUMERIC(6, 5) ENCODE ZSTD,
  SERVICE_FEE_RATE           NUMERIC(6, 5) ENCODE ZSTD,
  AMEX_GROSS_AMOUNT          NUMERIC(18, 2) ENCODE ZSTD,
  AMEX_ROC_COUNT             NUMERIC(18) ENCODE ZSTD,
  TRACKING_ID                NUMERIC(9, 0) ENCODE ZSTD,
  CPC_INDICATOR              VARCHAR(1) ENCODE RUNLENGTH,
  AMEX_ROC_COUNT_POA         NUMERIC(7, 0) ENCODE ZSTD,
  FOREIGN KEY (PAYMENT_NUMBER) 
     REFERENCES scratch.american_express_eptrn_payment (PAYMENT_NUMBER)
)
  DISTSTYLE KEY;

CREATE TABLE IF NOT EXISTS 
  scratch.american_express_eptrn_record_of_charges (
  AMEX_PAYEE_NUMBER          NUMERIC(18, 0) ENCODE ZSTD,
  AMEX_SE_NUMBER             NUMERIC(18, 0) ENCODE ZSTD,
  SE_UNIT_NUMBER             VARCHAR(10) ENCODE ZSTD,
  PAYMENT_YEAR               NUMERIC(4, 0) ENCODE ZSTD,
  PAYMENT_NUMBER             VARCHAR(8) DISTKEY ENCODE ZSTD,
  RECORD_TYPE                VARCHAR(1) ENCODE RUNLENGTH,
  DETAIL_RECORD_TYPE         VARCHAR(2) ENCODE RUNLENGTH,
  SE_BUSINESS_DATE           DATE ENCODE ZSTD,
  AMEX_PROCESS_DATE          DATE ENCODE ZSTD,
  SOC_INVOICE_NUMBER         NUMERIC(6, 0) ENCODE ZSTD,
  SOC_AMOUNT                 NUMERIC(18, 2) ENCODE ZSTD,
  ROC_AMOUNT                 NUMERIC(18, 2) ENCODE ZSTD,
  CM_NUMBER                  VARCHAR(19) ENCODE RUNLENGTH,
  CM_REF_NO                  VARCHAR(11) ENCODE RUNLENGTH,
  TRAN_DATE                  DATE ENCODE ZSTD,
  SE_REF_POA                 VARCHAR(30) PRIMARY KEY,
  NON_COMPLIANT_INDICATOR    VARCHAR(1) ENCODE RUNLENGTH,
  NON_COMPLIANT_ERROR_CODE_1 VARCHAR(4) ENCODE RUNLENGTH,
  NON_COMPLIANT_ERROR_CODE_2 VARCHAR(4) ENCODE RUNLENGTH,
  NON_COMPLIANT_ERROR_CODE_3 VARCHAR(4) ENCODE RUNLENGTH,
  NON_COMPLIANT_ERROR_CODE_4 VARCHAR(4) ENCODE RUNLENGTH,
  NON_SWIPED_INDICATOR       VARCHAR(1) ENCODE RUNLENGTH,
  CM_NUMBER_EXD              VARCHAR(19) ENCODE ZSTD,
  FOREIGN KEY (PAYMENT_NUMBER) 
     REFERENCES scratch.american_express_eptrn_payment (PAYMENT_NUMBER)
)
  DISTSTYLE KEY;

CREATE TABLE IF NOT EXISTS
  scratch.american_express_eptrn_adjustment(
  AMEX_PAYEE_NUMBER          NUMERIC(18, 0) ENCODE ZSTD,
  AMEX_SE_NUMBER             NUMERIC(18, 0) ENCODE ZSTD,
  SE_UNIT_NUMBER             VARCHAR(10) ENCODE ZSTD,
  PAYMENT_YEAR               NUMERIC(4, 0) ENCODE ZSTD,
  PAYMENT_NUMBER             VARCHAR(8) DISTKEY ENCODE ZSTD,
  RECORD_TYPE                VARCHAR(1) ENCODE RUNLENGTH,
  DETAIL_RECORD_TYPE         VARCHAR(2) ENCODE RUNLENGTH,
  AMEX_PROCESS_DATE          DATE ENCODE ZSTD SORTKEY ,
  ADJUSTMENT_NUMBER          VARCHAR(6) ENCODE ZSTD PRIMARY KEY,
  ADJUSTMENT_AMOUNT          NUMERIC(18, 2) ENCODE ZSTD,
  DISCOUNT_AMOUNT            NUMERIC(18, 2) ENCODE ZSTD,
  SERVICE_FEE_AMOUNT         NUMERIC(18, 2) ENCODE ZSTD,
  NET_ADJUSTMENT_AMOUNT      NUMERIC(18, 2) ENCODE ZSTD,
  DISCOUNT_RATE              NUMERIC(6, 5) ENCODE ZSTD,
  SERVICE_FEE_RATE           NUMERIC(6, 5) ENCODE ZSTD,
  CARDMEMBER_NUMBER          VARCHAR(19) ENCODE ZSTD ,
  ADJUSTMENT_REASON          VARCHAR(230) ENCODE ZSTD,
  FOREIGN KEY (PAYMENT_NUMBER)
     REFERENCES scratch.american_express_eptrn_payment (PAYMENT_NUMBER)
)
  DISTSTYLE KEY;
  

CREATE TABLE IF NOT EXISTS scratch.american_express_revenue_activity_chargeback_detail
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


CREATE TABLE IF NOT EXISTS scratch.american_express_epape_payment (
  SETTLEMENT_SE_ACCOUNT_NUMBER          NUMERIC(18, 0) ENCODE ZSTD,
  SETTLEMENT_ACCOUNT_NAME_CODE          VARCHAR(10) ENCODE ZSTD,
  SETTLEMENT_DATE                       DATE  ENCODE ZSTD SORTKEY,
  RECORD_TYPE                           VARCHAR(1) ENCODE RUNLENGTH,
  DETAIL_RECORD_TYPE                    VARCHAR(2) ENCODE RUNLENGTH,
  SETTLEMENT_AMOUNT                     NUMERIC(18, 2) ENCODE ZSTD,
  SE_BANK_SORT_CODE                     VARCHAR(15) ENCODE ZSTD,
  SE_BANK_ACCOUNT_NUMBER                VARCHAR(20) ENCODE ZSTD,
  SETTLEMENT_GROSS_AMOUNT               NUMERIC(18, 2) ENCODE ZSTD,
  TAX_AMOUNT                            NUMERIC(18, 2) ENCODE ZSTD,
  TAX_RATE                              NUMERIC(6, 5) ENCODE ZSTD,
  SERVICE_FEE_AMOUNT                    NUMERIC(18, 2) ENCODE ZSTD,
  SERVICE_FEE_RATE                      NUMERIC(6, 5) ENCODE ZSTD,
  SETTLEMENT_ADJUSTMENT_AMOUNT          NUMERIC(18, 2) ENCODE ZSTD,
  PAY_PLAN_SHORT_NAME                   VARCHAR(30) ENCODE ZSTD,
  PAYEE_NAME                            VARCHAR(30) ENCODE ZSTD,
  SETTLEMENT_ACCOUNT_NAME_CODE_EXTENDED VARCHAR(20) ENCODE ZSTD,
  SETTLEMENT_CURRENCY_CODE              VARCHAR(3) ENCODE ZSTD,
  PREVIOUS_DEBIT_BALANCE                NUMERIC(18, 2) ENCODE ZSTD,
  IBAN                                  VARCHAR(34) ENCODE ZSTD,
  BIC                                   VARCHAR(12) ENCODE ZSTD,
  GENERATED_PAYMENT_NUMBER              VARCHAR(36) ENCODE ZSTD DISTKEY,
  PRIMARY KEY (GENERATED_PAYMENT_NUMBER)
)
  DISTSTYLE KEY;
  
  
CREATE TABLE IF NOT EXISTS scratch.american_express_epape_adjustment (
  SETTLEMENT_SE_ACCOUNT_NUMBER          NUMERIC(18, 0) ENCODE ZSTD,
  SETTLEMENT_ACCOUNT_NAME_CODE          VARCHAR(10) ENCODE ZSTD,
  SETTLEMENT_DATE                       DATE ENCODE ZSTD SORTKEY,
  SUBMISSION_SE_ACCOUNT_NUMBER          NUMERIC(18, 0) ENCODE ZSTD,
  RECORD_TYPE                           VARCHAR(1) ENCODE RUNLENGTH,
  DETAIL_RECORD_TYPE                    VARCHAR(2) ENCODE RUNLENGTH,
  SUPPORTING_REFERENCE_NUMBER           NUMERIC(11, 0) ENCODE ZSTD,
  SETTLEMENT_GROSS_AMOUNT               NUMERIC(18, 0) ENCODE ZSTD,
  SETTLEMENT_DISCOUNT_AMOUNT            NUMERIC(18, 0) ENCODE ZSTD,
  SETTLEMENT_NET_AMOUNT                 NUMERIC(18, 0) ENCODE ZSTD,
  SERVICE_FEE_RATE                      NUMERIC(6, 5) ENCODE ZSTD,
  SETTLEMENT_TAX_AMOUNT                 NUMERIC(18, 0) ENCODE ZSTD,
  SETTLEMENT_TAX_RATE                   NUMERIC(6, 5) ENCODE ZSTD,
  CARD_MEMBER_ACCOUNT_NUMBER            VARCHAR(19) ENCODE ZSTD ,
  ADJUSTMENT_RECORD_MESSAGE_CODE        VARCHAR(10) ENCODE ZSTD ,
  ADJUSTMENT_MESSAGE_DESCRIPTION        VARCHAR(64) ENCODE ZSTD ,
  SUBMISSION_SE_BRANCH_NUMBER           VARCHAR(10) ENCODE ZSTD,
  SUBMISSION_GROSS_AMOUNT               NUMERIC(18, 0) ENCODE ZSTD,
  SUBMISSION_CURRENCY_CODE              VARCHAR(3) ENCODE ZSTD ,
  ADJUSTMENT_MESSAGE_REFERENCE          VARCHAR(15) ENCODE ZSTD ,
  GENERATED_PAYMENT_NUMBER              VARCHAR(36) ENCODE ZSTD DISTKEY,
  FOREIGN KEY (GENERATED_PAYMENT_NUMBER)
    REFERENCES  scratch.american_express_epape_payment (GENERATED_PAYMENT_NUMBER)
)
DISTSTYLE KEY;

CREATE TABLE IF NOT EXISTS scratch.american_express_epape_summary_of_charges (
  SETTLEMENT_SE_ACCOUNT_NUMBER       NUMERIC(18, 0) ENCODE ZSTD,
  SETTLEMENT_ACCOUNT_NAME_CODE       VARCHAR(10) ENCODE ZSTD,
  SETTLEMENT_DATE                    DATE ENCODE ZSTD SORTKEY,
  SUBMISSION_SE_ACCOUNT_NUMBER       NUMERIC(18, 0) ENCODE ZSTD,
  RECORD_TYPE                        VARCHAR(1) ENCODE RUNLENGTH,
  DETAIL_RECORD_TYPE                 VARCHAR(2) ENCODE RUNLENGTH,
  SOC_DATE                           DATE ENCODE ZSTD,
  SUBMISSION_CALCULATED_GROSS_AMOUNT NUMERIC(18, 0) ENCODE ZSTD,
  SUBMISSION_DECLARED_GROSS_AMOUNT   NUMERIC(18, 0) ENCODE ZSTD,
  DISCOUNT_AMOUNT                    NUMERIC(18, 0) ENCODE ZSTD,
  SETTLEMENT_NET_AMOUNT              NUMERIC(18, 0) ENCODE ZSTD,
  SERVICE_FEE_RATE                   NUMERIC(6, 5) ENCODE ZSTD,
  SETTLEMENT_GROSS_AMOUNT            NUMERIC(18, 0) ENCODE ZSTD,
  ROC_CALCULATED_COUNT               INTEGER ENCODE ZSTD,
  SETTLEMENT_TAX_AMOUNT              NUMERIC(18, 0) ENCODE ZSTD,
  SETTLEMENT_TAX_RATE                NUMERIC(6, 5) ENCODE ZSTD,
  SUBMISSION_CURRENCY_CODE           VARCHAR(3) ENCODE ZSTD,
  SUBMISSION_NUMBER                  NUMERIC(15, 0) ENCODE ZSTD,
  SUBMISSION_SE_BRANCH_NUMBER        VARCHAR(10) ENCODE ZSTD,
  SUBMISSION_METHOD_CODE             VARCHAR(2) ENCODE ZSTD,
  EXCHANGE_RATE                      NUMERIC(32, 18) ENCODE ZSTD,
  GENERATED_PAYMENT_NUMBER           VARCHAR(36) ENCODE ZSTD DISTKEY,
  GENERATED_SOC_NUMBER               VARCHAR(36) ENCODE ZSTD,
  PRIMARY KEY (GENERATED_SOC_NUMBER),
  FOREIGN KEY (GENERATED_PAYMENT_NUMBER) 
     REFERENCES scratch.american_express_epape_payment (GENERATED_PAYMENT_NUMBER)
)
  DISTSTYLE KEY;
  
  
CREATE TABLE IF NOT EXISTS scratch.american_express_epape_record_of_charges (
  SETTLEMENT_SE_ACCOUNT_NUMBER          NUMERIC(18, 0) ENCODE ZSTD,
  SETTLEMENT_ACCOUNT_NAME_CODE          VARCHAR(10) ENCODE ZSTD,
  SUBMISSION_SE_ACCOUNT_NUMBER          NUMERIC(18, 0) ENCODE ZSTD,
  RECORD_TYPE                           VARCHAR(1) ENCODE RUNLENGTH,
  DETAIL_RECORD_TYPE                    VARCHAR(2) ENCODE RUNLENGTH,
  CHARGE_AMOUNT                         NUMERIC(18, 0) ENCODE ZSTD,
  CHARGE_DATE                           DATE ENCODE ZSTD,
  ROC_REFERENCE_NUMBER                  VARCHAR(12) ENCODE ZSTD SORTKEY,
  ROC_REFERENCE_NUMBER_CPC_ONLY         VARCHAR(15) ENCODE ZSTD,
  THREE_DIGIT_CHARGE_AUTHORIZATION_CODE VARCHAR(3) ENCODE ZSTD,
  CARD_MEMBER_ACCOUNT_NUMBER            VARCHAR(19) ENCODE ZSTD,
  AIRLINE_TICKET_NUMBER                 VARCHAR(14) ENCODE ZSTD,
  SIX_DIGIT_CHARGE_AUTHORIZATION_CODE   VARCHAR(6) ENCODE ZSTD,
  GENERATED_PAYMENT_NUMBER              VARCHAR(36) ENCODE ZSTD DISTKEY,
  GENERATED_SOC_NUMBER                  VARCHAR(36) ENCODE ZSTD,
  PRIMARY KEY (ROC_REFERENCE_NUMBER),
  FOREIGN KEY (GENERATED_SOC_NUMBER)
      REFERENCES scratch.american_express_epape_summary_of_charges (GENERATED_SOC_NUMBER),
  FOREIGN KEY (GENERATED_PAYMENT_NUMBER)
      REFERENCES scratch.american_express_epape_payment (GENERATED_PAYMENT_NUMBER)
)
  DISTSTYLE KEY;