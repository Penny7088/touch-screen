#ifndef JKENCRYPTION_H_
#define JKENCRYPTION_H_


#include <string>
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
#include <jni.h>
#endif

using namespace std;




class JKEncryption{
public:
	/**获取16位大写MD5码*/
	static vector<char> MD5_byte(string tString);
	/**获取16位大写MD5码*/
	static string MD5_16(string tString);
	/**获取32位大写MD5码*/
	static string MD5_32(string tString);
	/**Aes加密*/
	static vector<char> AesEncryptor(vector<char> a_cIn,string tPassword);
	/**Aes解密*/
	static vector<char> AesDecryptor(vector<char> a_cIn,string tPassword);
	/**Base64加密*/
	static vector<char> Base64Encryptor(vector<char> a_cIn);
	/**Base64解密*/
	static vector<char> Base64Decryptor(vector<char> a_cIn);
	/**SHA1_40加密*/
	static string SHA1_40(string tString);
};

#endif /* JKENCRYPTION_H_ */
