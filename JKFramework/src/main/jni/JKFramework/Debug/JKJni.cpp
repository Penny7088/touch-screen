#include "JKStdafx.h"

string JKJni::toParameter(vector<string>a_tType)
{
	string tParameter = "(";
	for (unsigned int i=0; i<a_tType.size(); ++i)
	{
		if (i == a_tType.size() - 1)	//最后一个参数
			tParameter += ")";
		if (a_tType[i] == "string")
			tParameter += "Ljava/lang/String;";
		else if (a_tType[i] == "int")
			tParameter += "I";
		else if (a_tType[i] == "byte")
			tParameter += "B";
		else if (a_tType[i] == "byte[]")
			tParameter += "[B";
		else if (a_tType[i] == "void")
			tParameter += "V";
		else if (a_tType[i] == "float")
			tParameter += "F";
		else if (a_tType[i] == "object")
			tParameter += "Ljava/lang/Object;";
		else if (a_tType[i] == "bool")
			tParameter += "Z";
		else
			tParameter += a_tType[i];
	}
	return tParameter;
}

void JKJni::DoJavaFunction(jobject oObject,string tClass,string tFunction,vector<string>a_tType ,jvalue vValue[])
{
	JNIEnv * env = 	JKDebug::GetJNIEvent();

	jclass cClass = env->FindClass(tClass.c_str());
	jmethodID mMethod;
	if (oObject == NULL)
		mMethod = env->GetStaticMethodID(cClass, tFunction.c_str(),toParameter(a_tType).c_str());
	else
		mMethod = env->GetMethodID(cClass, tFunction.c_str(),toParameter(a_tType).c_str());
	if (oObject == NULL)
		env->CallStaticVoidMethodA(cClass, mMethod,vValue);
	else
		env->CallVoidMethodA(oObject, mMethod,vValue);

	env->DeleteLocalRef(cClass);
	JKDebug::ReleaseJNIEvent();
}

jobject JKJni::DoJavaFunction_object(jobject oObject,string tClass,string tFunction,vector<string>a_tType ,jvalue vValue[])
{
	JNIEnv * env = 	JKDebug::GetJNIEvent();

	jclass cClass = env->FindClass(tClass.c_str());
	jmethodID mMethod;
	if (oObject == NULL)
		mMethod = env->GetStaticMethodID(cClass, tFunction.c_str(),toParameter(a_tType).c_str());
	else
		mMethod = env->GetMethodID(cClass, tFunction.c_str(),toParameter(a_tType).c_str());
	jobject oBack = NULL;

	if (oObject == NULL)
		oBack = env->CallStaticObjectMethodA(cClass, mMethod,vValue);
	else
		oBack = env->CallObjectMethodA(oObject, mMethod,vValue);

	env->DeleteLocalRef(cClass);
	JKDebug::ReleaseJNIEvent();
	return oBack;
}

jfloat JKJni::DoJavaFunction_float(jobject oObject,string tClass,string tFunction,vector<string>a_tType ,jvalue vValue[])
{
	JNIEnv * env = 	JKDebug::GetJNIEvent();

	jclass cClass = env->FindClass(tClass.c_str());
	jmethodID mMethod;
	if (oObject == NULL)
		mMethod = env->GetStaticMethodID(cClass, tFunction.c_str(),toParameter(a_tType).c_str());
	else
		mMethod = env->GetMethodID(cClass, tFunction.c_str(),toParameter(a_tType).c_str());

	jfloat fBack;

	if (oObject == NULL)
		fBack = env->CallStaticFloatMethodA(cClass, mMethod,vValue);
	else
		fBack = env->CallFloatMethodA(oObject, mMethod,vValue);


	env->DeleteLocalRef(cClass);
	JKDebug::ReleaseJNIEvent();
	return fBack;
}

jboolean JKJni::DoJavaFunction_bool(jobject oObject,string tClass,string tFunction,vector<string>a_tType ,jvalue vValue[])
{
	JNIEnv * env = 	JKDebug::GetJNIEvent();

	jclass cClass = env->FindClass(tClass.c_str());
	jmethodID mMethod;
	if (oObject == NULL)
		mMethod = env->GetStaticMethodID(cClass, tFunction.c_str(),toParameter(a_tType).c_str());
	else
		mMethod = env->GetMethodID(cClass, tFunction.c_str(),toParameter(a_tType).c_str());

	jboolean bBack;

	if (oObject == NULL)
		bBack = env->CallStaticBooleanMethodA(cClass, mMethod,vValue);
	else
		bBack = env->CallBooleanMethodA(oObject, mMethod,vValue);


	env->DeleteLocalRef(cClass);
	JKDebug::ReleaseJNIEvent();
	return bBack;
}

jint JKJni::DoJavaFunction_int(jobject oObject,string tClass,string tFunction,vector<string>a_tType ,jvalue vValue[])
{
	JNIEnv * env = 	JKDebug::GetJNIEvent();

	jclass cClass = env->FindClass(tClass.c_str());
	jmethodID mMethod;
	if (oObject == NULL)
		mMethod = env->GetStaticMethodID(cClass, tFunction.c_str(),toParameter(a_tType).c_str());
	else
		mMethod = env->GetMethodID(cClass, tFunction.c_str(),toParameter(a_tType).c_str());

	jint nBack;

	if (oObject == NULL)
		nBack = env->CallStaticIntMethodA(cClass, mMethod,vValue);
	else
		nBack = env->CallIntMethodA(oObject, mMethod,vValue);


	env->DeleteLocalRef(cClass);
	JKDebug::ReleaseJNIEvent();
	return nBack;
}

jobject JKJni::NewJavaObject(string tClass)
{
	JNIEnv * env = 	JKDebug::GetJNIEvent();

	jclass cClass = env->FindClass(tClass.c_str());

	jobject oBack = NULL;
	oBack = env->NewGlobalRef(env->NewObject( cClass , env->GetMethodID(cClass , "<init>" ,"()V") ) );
	env->DeleteLocalRef(cClass);

	JKDebug::ReleaseJNIEvent();
	return oBack;
}
