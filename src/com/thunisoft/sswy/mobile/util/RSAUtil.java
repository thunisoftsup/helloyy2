package com.thunisoft.sswy.mobile.util;

/**
 * 
 */

import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import android.util.Log;

/**
 * RSA 工具类。提供加密，解密，生成密钥对等方法。
 * 
 */
public class RSAUtil {

    private static final String TAG = "RSAUtil";
    /**
     * 此实例只可创建一个，否则会有内存泄露
     */
    public static final BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();


    /****** app 登录 *******/
    public static final String LOGIN_APP_PUBLIC = "30819f300d06092a864886f70d010101050003818d00308189028181008db77285a817a696483d11d67307868fbfe4f2f4e786699100132132626c2c555d54d784886333f872b48a2ec3a891003160063cb01f9c2e3d1f8d1522870ce365d309662cc12f5b027e730de7cb222c1d63c53129622a6ab6644d56f4752efa85039d2c411c33c68e202938f92dedb7fa31f1df2623b5fc17bafef71cdc6d330203010001";
    public static final String LOGIN_APP_PRIVATE = "30820277020100300d06092a864886f70d0101010500048202613082025d020100028181008db77285a817a696483d11d67307868fbfe4f2f4e786699100132132626c2c555d54d784886333f872b48a2ec3a891003160063cb01f9c2e3d1f8d1522870ce365d309662cc12f5b027e730de7cb222c1d63c53129622a6ab6644d56f4752efa85039d2c411c33c68e202938f92dedb7fa31f1df2623b5fc17bafef71cdc6d33020301000102818067e226e01525292796d2b585b712b8301de1cdbb7b108bc80ffef5e1c2463b856402326de3b09cc32e0a6c14f5b6567c86b5363d3c7a1f79c1b2a42feefc8294019ebc271bc1584d5f09d8faa9a5f4f9a14f94869ffd97477ca9397cffbebec84a3ab538a2923a2999f14281c9f197fec2741959f47c4a354433d0735cd445a9024100d6fd9bc611ba8a871b206bde00abdccb57468d20cd9db29739d2eed0d254012bd9c857d1d663c9bfd39e57df7d4271069e9ffbc7e8395ea7602fc65390b6dc4d024100a8bfbb0af4160c8f2ff46d7197bb0c9e37f24a8c60b61f1e12e9099d2c7960cc587031a7fbd5a0c255442d676bf802923c3763977562ff3e426125a40bff2f7f024011804da8ff12776a6e2be27c0d03f09261056e41a85b5909747c96524b7ce9277fb00e6e178cc204aa9e6e3ad4408f60e192d6753c9347004b153888def3f3d50241009b60af20cecede91995e3ec7b12611e03d3363610e4ba32b55ce2d8403475fd1c83cc5fc1eedfd2ff2c2ebeba6869e17784aa7f0f5ad49c851e6917a1371cc91024100886f6ea8a14d70d33c6b34d68d54629d9fd8288e54e1e8a20fb07b03d05879afc0734606790e035f2eeceb24e17dd326b35c87546ab55004b7dc44bae0569012";

    /** app 二维码 **/
    public static final String EWM_PUBLIC = "30819f300d06092a864886f70d010101050003818d0030818902818100a088a1671e23f3777f419465952268f10fd206e550d4c63d0472649b6f3b6bc1a3e604e694ca6a997ee7b5b713bf17b1e20f528ffe810ef675929626efc49d841ba00b89ecb7dc48e75683f73ef6dc02413dc58f727f23067a80a469e0c4e7fb6dec7247c97342965d3a13a9072b9c8a36176683ec6c60f16061e4f450c0922b0203010001";
    public static final String EWM_PRIVATE = "30820277020100300d06092a864886f70d0101010500048202613082025d02010002818100a088a1671e23f3777f419465952268f10fd206e550d4c63d0472649b6f3b6bc1a3e604e694ca6a997ee7b5b713bf17b1e20f528ffe810ef675929626efc49d841ba00b89ecb7dc48e75683f73ef6dc02413dc58f727f23067a80a469e0c4e7fb6dec7247c97342965d3a13a9072b9c8a36176683ec6c60f16061e4f450c0922b02030100010281810082bf653db835adb918e34fbb00a11af9a359fb15d024cc24ac37832b7987daf6c6c7b41c1cf953a31ad21b9535b700d4777ae552b405672b628cf650561e3bef2c33c7ae47ab40983b570a0e0287555be6aadc0e0dabbff5547f3a36a85e9f9f6846bf3df8622afe49d5acd358cff819a23db6af01bfaead97c17571a8f321a1024100eb37658afad5f02b5f56d1145cc0e1d29dc3b4708fd8601ce2ac08db85a9c5184738f739960c31708f938c8ee181cfd3e8bd5c0a3672f43519fed6a9add20f7b024100aeb7e8ace6cbd5cc020625c91a190c44230a047e356899eb88874209b5455508ef645cb784b33efa0cb0b4e655036dd9027bbd0dc37797cec300c8883755311102402573ad9c62721c0a9be493e7dd18ed1bfef8c14b46ec19e99f81eaeb212e35eeb6b07054dffa5308ec237f36e4db297f41489d5616f358c4f7bda6ea5f7dd9ef02401c0c682d01ac2d795fc5cee116f07878a3cb62e743f9b3e40cf9f1cf0f2bbf37fcfe76a7fb84af12836106d9874252ab28ebe670c43579c876a3b48db19c5a91024100e412b703c57222fca7dd4e10d5611fb0d413f7cc5db27987450b9af68fa90e9d3957f30ada045dc6e847a96f73535a5512730381768bbf588599a3c5836b48c5";

    /** app 律协二维码 **/
    public static final String QRCODE_PUBLIC = "30819f300d06092a864886f70d010101050003818d00308189028181008c07190d60102717f454038790df8f7e09b7f4e6b664ac7ec36f91a8967810b7b51b6bc3fa1e724cd7923fc3074ea716b9534cfb5be55f035530ce16024c078bb1f1d1c881f7bd73cfb4bbf4cf55b8114645bde8591bb46a5da3f8833865d3fb7f8a46b7d8a333dee9f198103745b9170be45d789dbed46f29809a764b5c05250203010001";
    public static final String QRCODE_PRIVATE = "30820276020100300d06092a864886f70d0101010500048202603082025c020100028181008c07190d60102717f454038790df8f7e09b7f4e6b664ac7ec36f91a8967810b7b51b6bc3fa1e724cd7923fc3074ea716b9534cfb5be55f035530ce16024c078bb1f1d1c881f7bd73cfb4bbf4cf55b8114645bde8591bb46a5da3f8833865d3fb7f8a46b7d8a333dee9f198103745b9170be45d789dbed46f29809a764b5c052502030100010281800d137c9b6b2517017da77acce6127a6be2ac730a386e1e4d98228b0f9b3d8b121caf7ad8ca3a9da1df5fa0e4ad8202c87475faf25e1a854ce25fb45f09c7dfcc601f7e1df7bed2d5904b59814af305fc0e65145f75494a9a197f654df4c892201c5927b3ebb1a035060584a58df4d4537a42dcb530edfd56ee5d5e4826f542e1024100d7cdd4e94b4bc8d0a91cd83a41a69a532355277967e3f12bd5ea71d66c2db63d2eda86e0c31b96b5bf4268d18ae304fc3567dd1bd7f091046e14719275bcc34b024100a61c07afc8b58d626f2c0f5f584bb05a90a7852d488713c032ff841bcfa091d5cdca043aaa3677bcd13a9e37897bb4c2e2effedab24375bde7149fca8469a34f024100c1370bdab8cca88691f33e71a257fd32549f28519dbfcb2d5940a81db80b916fe42449c0b14f6137667f9a038a9b49da5a229d2811c5d4c127a1ec319a72a0530240235ae1f3f33759417342207f7dae9679a04257a13cc7ab578443485e1702292825a5f2e27daa1c302f2457daa0439944c5c2a92ee9345c922fe5fee0bef0311702404eb1f21c8f277a69eabd0651a952c80e76fc0829c20430f1067f89bab64c3157d05c1bda3a128784bd469614312e6ae712e4ac18b03c12253f726bc0a8e89025";
    
    /**
     * app 网上支付
     */
    public static final String QRCODE_PRIVATE_NEW = "30819f300d06092a864886f70d010101050003818d00308189028181008db77285a817a696483d11d67307868fbfe4f2f4e786699100132132626c2c555d54d784886333f872b48a2ec3a891003160063cb01f9c2e3d1f8d1522870ce365d309662cc12f5b027e730de7cb222c1d63c53129622a6ab6644d56f4752efa85039d2c411c33c68e202938f92dedb7fa31f1df2623b5fc17bafef71cdc6d330203010001";
    
    public static PublicKey getPublicKey(String keyStr) {
        KeyFactory keyFac = null;
        try {
            keyFac = KeyFactory.getInstance("RSA", bouncyCastleProvider);
            X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(Hex.decodeHex(keyStr.toCharArray()));
            return keyFac.generatePublic(pubSpec);
        } catch (Exception ex) {
            Log.e(TAG, "", ex);
        }
        return null;
    }

    public static PrivateKey getPrivateKey(String keyStr) {
        KeyFactory keyFac = null;
        try {
            keyFac = KeyFactory.getInstance("RSA", bouncyCastleProvider);
            PKCS8EncodedKeySpec priSpec = new PKCS8EncodedKeySpec(Hex.decodeHex(keyStr.toCharArray()));
            return keyFac.generatePrivate(priSpec);
        } catch (Exception ex) {
            Log.e(TAG, "", ex);
        }
        return null;
    }

    /**
     * 获得公钥模
     * 
     * @param pubKey
     * @return
     */
    public static String getPublicModulus(RSAPublicKey pubKey) {
        return pubKey.getModulus().toString(16);
    }

    /**
     * 获得公钥指数
     * 
     * @param pubKey
     * @return
     */
    public static String getPublicExponent(RSAPublicKey pubKey) {
        return pubKey.getPublicExponent().toString(16);
    }

    /**
     * * 生成密钥对 *
     * 
     * @return KeyPair *
     * @throws EncryptException
     */
    private static KeyPair generateKeyPair() throws Exception {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA", bouncyCastleProvider);
            final int KEY_SIZE = 1024;// 没什么好说的了，这个值关系到块加密的大小，可以更改，但是不要太大，否则效率会低
            keyPairGen.initialize(KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();
            KeyFactory keyFac = null;
            try {
                keyFac = KeyFactory.getInstance("RSA", bouncyCastleProvider);
            } catch (NoSuchAlgorithmException ex) {
                throw new Exception(ex.getMessage());
            }
            System.out.println(keyPair.getPublic());
            String pubStr = Hex.encodeHexString(keyPair.getPublic().getEncoded());
            System.out.println("public:===" + pubStr);
            X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(Hex.decodeHex(pubStr.toCharArray()));
            PublicKey pubKey = keyFac.generatePublic(pubSpec);
            System.out.println(pubKey);

            System.out.println(keyPair.getPrivate());
            String priStr = Hex.encodeHexString(keyPair.getPrivate().getEncoded());
            System.out.println("private:===" + priStr);
            PKCS8EncodedKeySpec priSpec = new PKCS8EncodedKeySpec(Hex.decodeHex(priStr.toCharArray()));
            PrivateKey priKey = keyFac.generatePrivate(priSpec);
            System.out.println(priKey);
            return keyPair;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * * 加密 *
     * 
     * @param key
     *            加密的密钥 *
     * @param data
     *            待加密的明文数据 *
     * @return 加密后的数据 *
     * @throws Exception
     */
    public static byte[] encrypt(PublicKey pk, byte[] data) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("RSA", bouncyCastleProvider);
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            int blockSize = cipher.getBlockSize();// 获得加密块大小，如：加密前数据为128个byte，而key_size=1024
            // 加密块大小为127
            // byte,加密后为128个byte;因此共有2个加密块，第一个127
            // byte第二个为1个byte
            int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
            int leavedSize = data.length % blockSize;
            int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
            byte[] raw = new byte[outputSize * blocksSize];
            int i = 0;
            while (data.length - i * blockSize > 0) {
                if (data.length - i * blockSize > blockSize)
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
                else
                    cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
                // 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到
                // ByteArrayOutputStream中，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了
                // OutputSize所以只好用dofinal方法。

                i++;
            }
            return raw;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 16进制 To byte[]
     * 
     * @param hexString
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     * 
     * @param c
     *            char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * * 解密 *
     * 
     * @param key
     *            解密的密钥 *
     * @param raw
     *            已经加密的数据 *
     * @return 解密后的明文 *
     * @throws Exception
     */
    public static byte[] decrypt(PrivateKey pk, byte[] raw) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("RSA", bouncyCastleProvider);
            cipher.init(Cipher.DECRYPT_MODE, pk);
            int blockSize = cipher.getBlockSize();
            ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
            int j = 0;

            while (raw.length - j * blockSize > 0) {
                bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
                j++;
            }
            return bout.toByteArray();
        } catch (Exception e) {
            Log.e(TAG, "", e);
            throw e;
        }
    }

    public static String decryptFromBytes(PrivateKey privateKey, byte[] raw) throws Exception {
        String result = "";
        byte[] de_result = RSAUtil.decrypt(privateKey, raw);
        StringBuffer sb = new StringBuffer();
        sb.append(new String(de_result));
        result = sb.toString();
        result = URLDecoder.decode(result, "UTF-8");
        return result;
    }

    public static String decrypt(PrivateKey privateKey, String mi) {
        String result = "";
        if (StringUtils.isBlank(mi)) {
            return mi;
        }
        byte[] en_result = hexStringToBytes(mi);
        try {
            byte[] de_result = RSAUtil.decrypt(privateKey, en_result);
            StringBuffer sb = new StringBuffer();
            sb.append(new String(de_result));
            result = sb.reverse().toString();
            result = URLDecoder.decode(result, "UTF-8");
            return result;
        } catch (Exception e) {
            Log.e(TAG, "解码出错", e);
        }
        return "";
    }

}
