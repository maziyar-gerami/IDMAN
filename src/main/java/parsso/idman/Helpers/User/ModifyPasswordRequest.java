package parsso.idman.Helpers.User;


import javax.naming.SizeLimitExceededException;
import javax.naming.ldap.ExtendedRequest;
import javax.naming.ldap.ExtendedResponse;
import java.io.Serializable;

public class ModifyPasswordRequest
        implements ExtendedRequest, Serializable {
    public static final String LDAP_EXOP_X_MODIFY_PASSWD =
            "1.3.6.1.4.1.4203.1.11.1";
    private static final byte LDAP_TAG_EXOP_X_MODIFY_PASSWD_ID =
            (byte) 0x80;
    private static final byte LDAP_TAG_EXOP_X_MODIFY_PASSWD_NEW =
            (byte) 0x82;
    private final String mDn;
    private final String mPassword;

    public ModifyPasswordRequest(String dn, String password)
            throws NullPointerException, SizeLimitExceededException {
        if (dn == null) {
            throw new NullPointerException("dn cannot be null");
        }

        if (password == null) {
            throw new NullPointerException("password cannot be null");
        }

        int dnlen = dn.length();
        int passlen = password.length();
        int totallen = 4 + dnlen + passlen;

        if (dnlen <= 0) {
            throw new SizeLimitExceededException("dn cannot be 0 length");
        }

        if (dnlen > 0xFF) {
            throw new SizeLimitExceededException(
                    "dn cannot be larger then 255 characters");
        }

        if (passlen <= 0) {
            throw new SizeLimitExceededException(
                    "password cannot be 0 length");
        }

        if (passlen > 0xFF) {
            throw new SizeLimitExceededException(
                    "password cannot be larger then 255 characters");
        }

        if (totallen > 0xFF) {
            throw new SizeLimitExceededException(
                    "the lengh of the dn + the lengh of the password cannot" +
                            " exceed 251 characters");
        }

        mDn = dn;
        mPassword = password;
    }

    public String getID() {
        return LDAP_EXOP_X_MODIFY_PASSWD;
    }

    public byte[] getEncodedValue() {
        byte[] password = mPassword.getBytes();
        byte[] dn = mDn.getBytes();

        // Sequence tag (1) + sequence length (1) + dn tag (1) +
        // dn length (1) + dn (variable) + password tag (1) +
        // password length (1) + password (variable)
        int encodedLength = 6 + dn.length + password.length;

        byte[] encoded = new byte[encodedLength];

        int i = 0;
        encoded[i++] = (byte) 0x30; // sequence start
        // length of body
        encoded[i++] = (byte) (4 + dn.length + password.length);

        encoded[i++] = LDAP_TAG_EXOP_X_MODIFY_PASSWD_ID;
        encoded[i++] = (byte) dn.length;

        System.arraycopy(dn, 0, encoded, i, dn.length);
        i += dn.length;

        encoded[i++] = LDAP_TAG_EXOP_X_MODIFY_PASSWD_NEW;
        encoded[i++] = (byte) password.length;

        System.arraycopy(password, 0, encoded, i, password.length);
        i += password.length;

        return encoded;
    }

    public ExtendedResponse createExtendedResponse(String id,
                                                   byte[] berValue,
                                                   int offset, int length) {
        return null;
    }
}