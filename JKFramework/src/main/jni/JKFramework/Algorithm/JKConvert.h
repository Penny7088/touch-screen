#ifndef JKCONVERT_H_
#define JKCONVERT_H_

#include <string>
#include <vector>
#include <ctype.h>
#include <stdio.h>
#include <jni.h>
//#include <iconv.h>


using namespace std;


class JKConvert{
public:
	/**整型转换为字符串*/
	static string toString(int nNum);
	/**Double转换为字符串*/
	static string toString(double dNum);
	/**Float转换为字符串*/
	static string toString(float fNum);
	/**长整型转换为字符串*/
	static string toString(long long lNum);
	/**Jstring转string(需手动释放)*/
	static string toString(jstring tText);
	/**字节数组转string*/
	static string toString(vector<char> a_byList);
	/**字节数组转string*/
	static string toString(vector<char> a_byList,string tEncoding);
	/**字符串转为整型*/
	static int toInt(string tText);
	/**字符串转为整型*/
	static long long toLong(string tText);
	/**字符串转为双精度浮点型*/
	static double toDouble(string tText);
	/**字符串转为浮点型*/
	static float toFloat(string tText);
	/**char*转动态数组*/
	static vector<char> toCharArray(char* pIn,int nLength);
	/**char*转动态数组*/
	static vector<char> toCharArray(const char* pIn,int nLength);
	/**jbyteArray转动态数组*/
	static vector<char> toCharArray(jbyteArray a_byArray);
	/**字符串转动态数组*/
	static vector<char> toCharArray(string tText);
	/**字符串转动态数组*/
	static vector<char> toCharArray(string tText,string tEncoding);
	/**动态数组转char*/
	static void toCharArray(vector<char> a_cBuffer,char* pOut);
	/**string转Jstring(需手动释放)*/
	static jstring toJString(string tText);
	/**动态数组转JbyteArray(需手动释放)*/
	static jbyteArray toJCharArray(vector<char> a_cBuffer);
	/**动态数组转JbyteArray(需手动释放)*/
	static jbyteArray toJCharArray(vector<jbyte> a_byBuffer);
	/**动态数组转JintArray(需手动释放)*/
	static jintArray toJIntArray(vector<int> a_nBuffer);
	/**字符串转换成URL（默认utf-8)*/
	static string toUrl(string tText);
	/**字符串转换成URL*/
	static string toUrl(string tText,string tEncoding);
	/**URL（默认utf-8)转换成string*/
	static string URLToString(string tText);
	/**URL 转换成string*/
	static string URLToString(string tText,string tEncoding);
};

#endif /* JKCONVERT_H_ */
