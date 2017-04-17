#include "JKStdafx.h"


void JKFile::CreateDir(string tDirName)
{
	int nIndex = tDirName.find_last_of(GetFileSepartor());
	if (nIndex >= 0)
	{
		string tTmpPath = tDirName.substr(0, nIndex);
		if (!JKFile::IsExists(tTmpPath.c_str()))	//文件不存在
			JKFile::CreateDir(tTmpPath.c_str());
		else
			return;
		mkdir(tTmpPath.c_str(), S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH);
	}
}

bool JKFile::IsExists(string tFileName)
{
	int nBack = -1;
	nBack = access(tFileName.c_str(),0);
	if (0 == nBack)
		return true;
	else
		return false;
}

long long JKFile::GetFileSize(string tFileName)
{
	struct stat statbuff;
	if(stat(tFileName.c_str(), &statbuff) < 0){
		return -1;
	}else{
		return statbuff.st_size;
	}
}

string JKFile::GetFileSepartor()
{
    return "/";
}

string JKFile::ReadFile(string tFileName)
{
	return ReadFile(tFileName, "UTF-8");
}

string JKFile::ReadFile(string tFileName,string tEncoding)
{
	vector<char> a_byList = JKFile::ReadBytes(tFileName);
	return JKConvert::toString(a_byList, tEncoding);
}

vector<char> JKFile::ReadBytes(string tFileName)
{
	vector<char> a_cBack(0);
	unsigned long filesize = GetFileSize(tFileName);

	char *p = new char[filesize];
	memset(p, 0, filesize);
	FILE *fp = NULL;
	if ((fp = fopen(tFileName.c_str(), "rb+")) == NULL) {
		delete[]p;
		return a_cBack;
	}
	fread(p, 1, filesize, fp);
	fflush(fp);
	fclose(fp);
	//Bom判断，如果长度小于3 默认没有bom
	//utf8的bom 前3个字节为0xbf 0xbb 0xef
	if (filesize > 3 && ((*(int*)p) & 0x00ffffff) == 0x00bfbbef)
	{
		a_cBack = JKConvert::toCharArray(p + 3, filesize - 3);
	}
	else
	{
		a_cBack = JKConvert::toCharArray(p, filesize);
	}
	delete[] p;
	return a_cBack;
}

void JKFile::WriteFile(string tFileName,vector<char> a_cText)
{
	CreateDir(tFileName);
	FILE *fp = NULL;
	if ((fp = fopen(tFileName.c_str(), "wb+")) == NULL) {
		return;
	}
	fwrite(&a_cText[0], 1, a_cText.size(), fp);
	fflush(fp);
	fclose(fp);
}

void JKFile::WriteFile(string tFileName,string tText)
{
	WriteFile(tFileName, tText, "UTF-8");
}

void JKFile::WriteFile(string tFileName,string tText,string tCharacter)
{
	CreateDir(tFileName);
	vector<char> a_cText = JKConvert::toCharArray(tText,tCharacter);
	char* p = new char [a_cText.size() + 1];
	JKConvert::toCharArray(a_cText,p);

	FILE *fp = NULL;
	if ( (fp = fopen(tFileName.c_str(), "wb+")) == NULL) {
		return;
	}
	fwrite(p,a_cText.size(),1,fp);
	fflush(fp);
	fclose(fp);


	delete [] p;
}

string JKFile::GetCurrentPath()
{
	JNIEnv* env = JKDebug::GetJNIEvent();

	vector<string> a_tType;
	a_tType.push_back("string");
	jvalue v[0];

	jobject a_byBack = JKJni::DoJavaFunction_object(NULL,"com/jkframework/algorithm/JKFile","GetCurrentPath",a_tType,v);
	string tBack = JKConvert::toString((jstring)a_byBack);

	JKDebug::ReleaseJNIEvent();
	return tBack;
}

string JKFile::GetPublicPath()
{
	JNIEnv* env = JKDebug::GetJNIEvent();

	vector<string> a_tType;
	a_tType.push_back("string");
	jvalue v[0];

	jobject a_byBack = JKJni::DoJavaFunction_object(NULL,"com/jkframework/algorithm/JKFile","GetPublicPath",a_tType,v);
	string tBack = JKConvert::toString((jstring)a_byBack);

	JKDebug::ReleaseJNIEvent();
	return tBack;
}

vector<char> JKFile::ReadAssets(jobject assetManager,string tPath)
{
	vector<char> a_cBack(0);
	JNIEnv * env = 	JKDebug::GetJNIEvent();
	AAssetManager* gAssetMgr  = AAssetManager_fromJava(env, assetManager);

	AAsset* asset = AAssetManager_open(gAssetMgr, tPath.c_str(), AASSET_MODE_UNKNOWN);
	if( asset == NULL)
		return a_cBack;
	long lLen = AAsset_getLength(asset);

	char *p = new char[lLen];
	memset(p, 0, lLen);
	AAsset_read(asset, p ,lLen);
	a_cBack = JKConvert::toCharArray(p,lLen);

	AAsset_close(asset);
	delete[] p;
	JKDebug::ReleaseJNIEvent();

	return a_cBack;
}
