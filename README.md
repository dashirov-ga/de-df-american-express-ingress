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
