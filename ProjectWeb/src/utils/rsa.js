//rsa加密
import {JSEncrypt} from "jsencrypt"

const publicKey =
    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDC3hmVkDLfCzRCgoYEshQR5OPz" +
    "zFXM5Tk4eq8gyAnhptjH2Da0EzSQ2I0PMftS21acCjIBSP6LGyl2nsBbPj1lfSWP" +
    "uLoT6EUEwwaIllMZF7pp3DA0eE7O8vHG4pjGqepO+UlnlbvTe64pQJz4e4nYn0qm" +
    "9EJHcpyQz1LSY3QipQIDAQAB";


/**
 * rsa加密函数
 * @param data  要加密的字符串
 */
export const encodeData=(data)=>{
    let encryptor = new JSEncrypt()
    // 设置公钥
    encryptor.setPublicKey(publicKey)
    return encryptor.encrypt(data)
}
