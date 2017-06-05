package co.ga.batch;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Custom Context: Google Account ID
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "account"
})
public class GoogleAdWordsAccountContext extends JsonSelfDescribingContext implements Serializable {
    private static final String iglu = "iglu:co.ga.batch/google_adwords_account_ctx/1-0-0";

    /**
     * Unique Google AdWords Account Identifier
     * (Required)
     */
    @JsonProperty("account")
    @JsonPropertyDescription("Unique Google AdWords Account Identifier")
    @Size(max = 36)
    @NotNull
    private String account;
    private final static long serialVersionUID = -2753749323613418321L;

    /**
     * No args constructor for use in serialization
     */
    public GoogleAdWordsAccountContext() {
        super(iglu);
    }

    /**
     * @param account
     */
    public GoogleAdWordsAccountContext(String account) {
        super(iglu);
        this.account = account;
    }

    /**
     * Unique Google AdWords Account Identifier
     * (Required)
     */
    @JsonProperty("account")
    public String getAccount() {
        return account;
    }

    /**
     * Unique Google AdWords Account Identifier
     * (Required)
     */
    @JsonProperty("account")
    public void setAccount(String account) {
        this.account = account;
    }

    public GoogleAdWordsAccountContext withAccount(String account) {
        this.account = account;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(account).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GoogleAdWordsAccountContext)) {
            return false;
        }
        GoogleAdWordsAccountContext rhs = ((GoogleAdWordsAccountContext) other);
        return new EqualsBuilder().append(account, rhs.account).isEquals();
    }

}
