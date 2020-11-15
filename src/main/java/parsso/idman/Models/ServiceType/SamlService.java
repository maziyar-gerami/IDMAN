package parsso.idman.Models.ServiceType;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.Models.ServicesSubModel.AttributeReleasePolicy;
import parsso.idman.Models.Service;

@Setter
@Getter
public class SamlService extends Service {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String metadataLocation;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String metadataExpirationDuration;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String requireSignedRoot;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    boolean signAssertions;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    boolean signResponses;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    boolean encryptionOptional;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    boolean encryptAssertions;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    boolean encryptAttributes;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    boolean encryptableAttributes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String requiredAuthenticationContextClass;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String requiredNameIdFormat;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String skewAllowance;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String metadataCriteriaPattern;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String metadataCriteriaDirection;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    boolean metadataCriteriaRoles;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    boolean metadataCriteriaRemoveEmptyEntitiesDescriptors;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    boolean metadataCriteriaRemoveRolelessEntityDescriptors;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String attributeNameFormats;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String attributeFriendlyNames;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String attributeValueTypes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String nameIdQualifier;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String issuerEntityId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String assertionAudiences;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String serviceProviderNameIdQualifier;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    boolean skipGeneratingAssertionNameId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    boolean skipGeneratingTransientNameId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    boolean skipGeneratingSubjectConfirmationInResponseTo;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    boolean skipGeneratingSubjectConfirmationNotOnOrAfter;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    boolean skipGeneratingSubjectConfirmationRecipient;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    boolean skipGeneratingSubjectConfirmationNotBefore;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    boolean skipGeneratingSubjectConfirmationNameId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String signingCredentialFingerprint;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String signingCredentialType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String signingSignatureReferenceDigestMethods;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String signingSignatureAlgorithms;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String signingSignatureBlackListedAlgorithms;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String signingSignatureWhiteListedAlgorithms;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String signingSignatureCanonicalizationAlgorithm;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String encryptionDataAlgorithms;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String encryptionKeyAlgorithms;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String encryptionBlackListedAlgorithms;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String encryptionWhiteListedAlgorithms;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String whiteListBlackListPrecedence;

    public SamlService(){
        super.setAtClass("org.apereo.cas.support.saml.services.SamlRegisteredService");
    }
}
