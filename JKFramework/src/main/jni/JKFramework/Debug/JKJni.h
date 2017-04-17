#ifndef JKJNI_H_
#define JKJNI_H_

#include <string>
#include <jni.h>
#include <vector>

using namespace std;



class JKJni{
public:		//函数
	/**调用Java层
	 * Type类型前面为参数类型,最后一个为返回类型(void,int,string,byte[])
	 */
	static void DoJavaFunction(jobject oObject,string tClass,string tFunction,vector<string>a_tType,jvalue vValue[]);
	/**调用java层返回object型*/
	static jobject DoJavaFunction_object(jobject oObject,string tClass,string tFunction,vector<string>a_tType,jvalue vValue[]);
	/**调用Java层 返回浮点型*/
	static jfloat DoJavaFunction_float(jobject oObject,string tClass,string tFunction,vector<string>a_tType,jvalue vValue[]);
	/**调用Java层 返回布尔型*/
	static jboolean DoJavaFunction_bool(jobject oObject,string tClass,string tFunction,vector<string>a_tType,jvalue vValue[]);
	/**调用Java层 返回整型*/
	static jint DoJavaFunction_int(jobject oObject,string tClass,string tFunction,vector<string>a_tType,jvalue vValue[]);
	/**调用Java层 返回new的object型*/
	static jobject NewJavaObject(string tClass);
private:
	static string toParameter(vector<string>a_tType);
};

#endif /* JKJNI_H_ */
