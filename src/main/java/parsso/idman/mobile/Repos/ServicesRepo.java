package parsso.idman.mobile.Repos;

import com.google.zxing.WriterException;

import java.io.IOException;

public interface ServicesRepo {
    public byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException;
}
