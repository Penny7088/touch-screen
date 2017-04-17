#ifndef JKFILE_H_
#define JKFILE_H_

#include <string>
#include <vector>
#include <jni.h>

using namespace std;


class JKFile{
public:
	/**文件是否存在*/
	static bool IsExists(string tFileName);
	/**创建当前文件路径下的文件夹*/
	static void CreateDir(string tDirName);
	/**获取文件大小*/
	static long long GetFileSize(string tFileName);
	/**读取文件字节数组*/
	static vector<char> ReadAssets(jobject assetManager,string tPath);
	/**读取文件字符串(默认UTF-8)*/
	static string ReadFile(string tFileName);
	/**读取文件字符串(支持UTF-8,GBK)*/
	static string ReadFile(string tFileName,string tCharacter);
	/**读取文件字节数组*/
	static vector<char> ReadBytes(string tFileName);
	/**写入文件字符数组*/
	static void WriteFile(string tFileName,vector<char> a_cText);
	/**写入文件字符串(默认UTF-8)*/
	static void WriteFile(string tFileName,string tText);
	/**写入文件字符串(支持UTF-8,GBK)*/
	static void WriteFile(string tFileName,string tText,string tCharacter);
	/**获取当前平台下的应用路径(末尾不带/)*/
	static string GetCurrentPath();
	/**获取当前平台跟随用路径(末尾不带/)*/
	static string GetPublicPath();
    /**获取分割线*/
    static string GetFileSepartor();

};


#endif /* JKFILE_H_ */
