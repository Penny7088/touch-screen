#include "JKStdafx.h"

string JKConvert::toString(int nNum)
{
	char buffer[20];
	sprintf(buffer,"%d",nNum);
	string str(buffer);
	return str;
}

string JKConvert::toString(double dNum)
{
	char buffer[40];
	sprintf(buffer,"%.16f",dNum);
	string str(buffer);
	for (unsigned int i=str.size()-1;;--i)
	{
		if (str.at(i) == '0')
		{
			str.erase(str.begin()+i);
		}
		else
		{
			break;
		}
	}
	return str;
}

string JKConvert::toString(float fNum)
{
	char buffer[40];
	sprintf(buffer,"%.7f",fNum);
	string str(buffer);
	for (unsigned int i=str.size()-1;;--i)
	{
		if (str.at(i) == '0')
		{
			str.erase(str.begin()+i);
		}
		else
		{
			break;
		}
	}
	return str;
}

string JKConvert::toString(long long lNum)
{
	char buffer[40];
	sprintf(buffer,"%lld",lNum);
	string str(buffer);
	return str;
}

string JKConvert::toString(jstring tText)
{
	JNIEnv * env = 	JKDebug::GetJNIEvent();

	const char* a_cBack = (env)->GetStringUTFChars(tText,NULL);
	string tBack(a_cBack);
	env->ReleaseStringUTFChars(tText, a_cBack);

	JKDebug::ReleaseJNIEvent();
	return tBack;
}

int JKConvert::toInt(string tText)
{
	return atoi(tText.c_str());
}

long long JKConvert::toLong(string tText)
{
	return atoll(tText.c_str());
}

double JKConvert::toDouble(string tText)
{
	return atof(tText.c_str());
}

float JKConvert::toFloat(string tText)
{
	return atof(tText.c_str());
}

string JKConvert::toString(vector<char> a_byList)
{
	return JKConvert::toString(a_byList,"UTF-8");
}

string JKConvert::toString(vector<char> a_byList,string tEncoding)
{
	JNIEnv* env = JKDebug::GetJNIEvent();

	jbyteArray a_byInlist = toJCharArray(a_byList);
	jstring tChar =  toJString(tEncoding);

	vector<string> a_tType;
	a_tType.push_back("byte[]");
	a_tType.push_back("string");
	a_tType.push_back("string");
	jvalue v[2];
	v[0].l = a_byInlist;
	v[1].l = tChar;

	jobject a_byBack = JKJni::DoJavaFunction_object(NULL,"com/jkframework/algorithm/JKConvert","toString",a_tType,v);

	string tBack = toString((jstring)a_byBack);

	env->DeleteLocalRef(a_byInlist);
	env->DeleteLocalRef(tChar);

	JKDebug::ReleaseJNIEvent();
	return tBack;
//	char* pOut = new char[a_byList.size() * 4 + 1];
//	memset(pOut,0 ,a_byList.size()*4+1);
//	char* pp = pOut;
//
//	iconv_t cd=iconv_open("UTF-8",tEncoding);
//	iconv(cd,&a_byList, a_byList.size(), &pp, a_byList.size()*4+1);
//	iconv_close (cd);
//	string tBack = "";
//	delete[] pOut;
//	return tBack;
}

vector<char> JKConvert::toCharArray(char* pIn,int nLength)
{
	vector<char> a_cBuffer(nLength);
	if (nLength > 0)
	{
		memcpy(&a_cBuffer[0], pIn, nLength);
	}
	return a_cBuffer;
}

vector<char> JKConvert::toCharArray(const char* pIn,int nLength)
{
	vector<char> a_cBuffer(nLength);
	if (nLength > 0)
	{
		memcpy(&a_cBuffer[0], pIn, nLength);
	}
	return a_cBuffer;
}

void JKConvert::toCharArray(vector<char> a_cBuffer,char* pOut)
{
	int nLength = a_cBuffer.size();
	if (nLength > 0)
	{
		memcpy(pOut, &a_cBuffer[0], nLength);
	}
}

vector<char> JKConvert::toCharArray(jbyteArray a_byArray)
{
	JNIEnv * env = 	JKDebug::GetJNIEvent();

	jbyte* a_byOut = env->GetByteArrayElements(a_byArray,JNI_FALSE);
	jsize lOut = env->GetArrayLength(a_byArray);

	char * a_cOut = new char[lOut + 1];
	memset(a_cOut,0,lOut+1);
	memcpy(a_cOut, a_byOut, lOut);
	vector<char> a_cBack = toCharArray(a_cOut,lOut);
	delete [] a_cOut;

	env->ReleaseByteArrayElements((jbyteArray)a_byArray, a_byOut, JNI_FALSE);

	JKDebug::ReleaseJNIEvent();
	return a_cBack;
}

vector<char> JKConvert::toCharArray(string tText)
{
	return JKConvert::toCharArray(tText,"UTF-8");
}

vector<char> JKConvert::toCharArray(string tText,string tEncoding)
{
	JNIEnv* env = JKDebug::GetJNIEvent();

	jstring tString = toJString(tText);
	jstring tChar =  toJString(tEncoding);

	vector<string> a_tType;
	a_tType.push_back("string");
	a_tType.push_back("string");
	a_tType.push_back("byte[]");
	jvalue v[2];
	v[0].l = tString;
	v[1].l = tChar;

	jobject a_byBack = JKJni::DoJavaFunction_object(NULL,"com/jkframework/algorithm/JKConvert","toByteArray",a_tType,v);

	vector<char> a_cBack = toCharArray((jbyteArray)a_byBack);

	env->DeleteLocalRef(tString);
	env->DeleteLocalRef(tChar);

	JKDebug::ReleaseJNIEvent();
	return a_cBack;
}

jbyteArray JKConvert::toJCharArray(vector<char> a_cBuffer)
{
	JNIEnv * env = 	JKDebug::GetJNIEvent();

	char * a_cChar = new char[a_cBuffer.size() + 1];
	toCharArray(a_cBuffer,a_cChar);
	jbyteArray a_byInlist = env->NewByteArray(a_cBuffer.size());
	env->SetByteArrayRegion(a_byInlist, 0, a_cBuffer.size() , (jbyte*)a_cChar);

	JKDebug::ReleaseJNIEvent();
	return a_byInlist;
}

jbyteArray JKConvert::toJCharArray(vector<jbyte> a_byBuffer)
{
	JNIEnv * env = 	JKDebug::GetJNIEvent();

	jbyteArray a_byInlist = env->NewByteArray(a_byBuffer.size());
	env->SetByteArrayRegion(a_byInlist, 0, a_byBuffer.size(), &a_byBuffer.front());

	JKDebug::ReleaseJNIEvent();
	return a_byInlist;
}


jintArray JKConvert::toJIntArray(vector<int> a_nBuffer)
{
	JNIEnv * env = 	JKDebug::GetJNIEvent();

	jintArray a_byInlist = env->NewIntArray(a_nBuffer.size());
	env->SetIntArrayRegion(a_byInlist, 0, a_nBuffer.size(), &a_nBuffer.front());
	JKDebug::ReleaseJNIEvent();
	return a_byInlist;
}

jstring JKConvert::toJString(string tText)
{
	JNIEnv * env = 	JKDebug::GetJNIEvent();
	jstring tKey = env->NewStringUTF(tText.c_str());
	JKDebug::ReleaseJNIEvent();
	return tKey;
}

string JKConvert::toUrl(string tText,string tEncoding)
{
	string tInput;
	if (JKAnalysis::toUpper(tEncoding) == "UTF-8")
	{
		tInput = tText;
	}
	else if (JKAnalysis::toUpper(tEncoding) == "GBK")
	{
		vector<char> a_cBack;
		a_cBack = toCharArray(tText.c_str(),tText.length());
		tInput = JKConvert::toString(a_cBack,"gbk");
	}
	string tBack;
	size_t len=tInput.length();
	for (size_t i=0;i<len;i++)
	{
		if(isalnum((unsigned char)tInput.at(i)))
		{
			char tempbuff[2]={0};
			sprintf(tempbuff,"%c",(unsigned char)tInput.at(i));
			tBack.append(tempbuff);
		}
		else if (isspace((unsigned char)tInput.at(i)))
		{
			tBack.append("+");
		}
		else
		{
			char tempbuff[4];
			sprintf(tempbuff,"%%%X%X",((unsigned char)tInput.at(i)) >>4,((unsigned char)tInput.at(i)) %16);
			tBack.append(tempbuff);
		}
	}
	return tBack;
}


string JKConvert::toUrl(string tText)
{
	return toUrl(tText , "utf-8");
}

string JKConvert::URLToString(string tText)
{
	return URLToString(tText,"utf-8");
}

string JKConvert::URLToString(string tText,string tEncoding)
{
	string tOutput="";
	char tmp[2];
	int i=0,idx=0,len=tText.length();
	while(i<len){
		if(tText[i]=='%'){
			tmp[0]=tText[i+1];
			tmp[1]=tText[i+2];

			char tmp1 = -1;
			char tmp2 = -1;

			if(tmp[0]>='0' && tmp[0]<='9')
				tmp1 = (char)(tmp[0]-'0');
			else if(tmp[0]>='a' && tmp[0]<='f')
				tmp1 = (char)(tmp[0]-'a'+10);
			else if(tmp[0]>='A' && tmp[0]<='F')
				tmp1 =  (char)(tmp[0]-'A'+10);

			if(tmp[1]>='0' && tmp[1]<='9')
				tmp2 = (char)(tmp[1]-'0');
			else if(tmp[1]>='a' && tmp[1]<='f')
				tmp2 = (char)(tmp[1]-'a'+10);
			else if(tmp[1]>='A' && tmp[1]<='F')
				tmp2 =  (char)(tmp[1]-'A'+10);

			char tempWord[2];
			char chn;
			tempWord[0] = tmp1;                         //make the B to 11 -- 00001011
			tempWord[1] = tmp2;                         //make the 0 to 0  -- 00000000
			chn = (tempWord[0] << 4) | tempWord[1];                //to change the BO to 10110000

			tOutput += chn;
			i=i+3;
		}
		else if(tText[i]=='+'){
			tOutput+=' ';
			i++;
		}
		else{
			tOutput+=tText[i];
			i++;
		}
	}
	string tBack;
	if (JKAnalysis::toUpper(tEncoding) == "UTF-8")
	{
		tBack = tOutput;
	}
	else if (JKAnalysis::toUpper(tEncoding) == "GBK")
	{
		vector<char> a_cBack;
		a_cBack = toCharArray(tOutput.c_str(),tOutput.length());
		tBack = JKConvert::toString(a_cBack,"gbk");
	}
	return tBack;
}
