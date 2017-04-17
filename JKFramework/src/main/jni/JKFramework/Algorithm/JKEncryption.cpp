#include "JKStdafx.h"


string JKEncryption::MD5_32(string tString)
{
	MD5 md5;
	md5.update(tString);
	return JKAnalysis::toUpper(md5.toString());
}

string JKEncryption::MD5_16(string tString)
{
	return MD5_32(tString).substr(8,16);
}

vector<char> JKEncryption::MD5_byte(string tString)
{
    MD5 md5;
    md5.update(tString);
    const byte *input = md5.digest();
	vector<char>a_cBack;
	for (int i=0;i<16;++i)
		a_cBack.push_back(input[i]);
	return a_cBack;
}

vector<char> JKEncryption::AesEncryptor(vector<char> a_cIn,string tPassword)
{	
	int nFilLength = a_cIn.size();
	if (nFilLength % 16 != 0) {
		nFilLength = nFilLength + (16 - (nFilLength % 16));
	}
	char* pIn = new char[nFilLength];
	char* pOut = new char[nFilLength];
	memset(pIn, 0, nFilLength);
	memset(pOut, 0, nFilLength);
	JKConvert::toCharArray(a_cIn,pIn);
	aes_encrypt_ctx ctx[1];

	aes_encrypt_key((unsigned char*)MD5_16(tPassword).c_str(),16,ctx);
	aes_cbc_encrypt((const unsigned char*)pIn,(unsigned char*)pOut,nFilLength,(unsigned char*)MD5_16(tPassword).c_str(),ctx);

	vector<char> a_cBack = JKConvert::toCharArray(pOut,nFilLength);
	delete[] pIn;
	delete[] pOut;
	return a_cBack;
}


vector<char> JKEncryption::AesDecryptor(vector<char> a_cIn,string tPassword)
{
	char* pIn = new char[a_cIn.size()];
	char* pOut = new char[a_cIn.size()];
	memset(pOut, 0, a_cIn.size());
	JKConvert::toCharArray(a_cIn,pIn);
	aes_decrypt_ctx ctx[1];

	aes_decrypt_key((unsigned char*)MD5_16(tPassword).c_str(),16,ctx);
	aes_cbc_decrypt((const unsigned char*)pIn,(unsigned char*)pOut,a_cIn.size(),(unsigned char*)MD5_16(tPassword).c_str(),ctx);
	
	vector<char> a_cBack = JKConvert::toCharArray(pOut,a_cIn.size());
	int nRealSize = 0;
	for (int i = a_cBack.size() - 1; i >= 0; i--)
	{
		if (a_cBack[i] == '\0')
			continue;
		else 
		{
			nRealSize = i+1; 
			break;
		}
	}
	a_cBack.resize(nRealSize);
	delete[] pIn;
	delete[] pOut;

	return a_cBack;
}

vector<char> JKEncryption::Base64Encryptor(vector<char> a_cIn)
{
	char *Data = &a_cIn.at(0);
	int DataByte = a_cIn.size();
	//编码表
	const char EncodeTable[]="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	//返回值
	string strEncode;
	unsigned char Tmp[4]={0};
	int LineLength=0;
	for(int i=0;i<(int)(DataByte / 3);i++)
	{
		Tmp[1] = *Data++;
		Tmp[2] = *Data++;
		Tmp[3] = *Data++;
		strEncode+= EncodeTable[Tmp[1] >> 2];
		strEncode+= EncodeTable[((Tmp[1] << 4) | (Tmp[2] >> 4)) & 0x3F];
		strEncode+= EncodeTable[((Tmp[2] << 2) | (Tmp[3] >> 6)) & 0x3F];
		strEncode+= EncodeTable[Tmp[3] & 0x3F];
		if(LineLength+=4,LineLength==76) {strEncode+="\r\n";LineLength=0;}
	}
	//对剩余数据进行编码
	int Mod=DataByte % 3;
	if(Mod==1)
	{
		Tmp[1] = *Data++;
		strEncode+= EncodeTable[(Tmp[1] & 0xFC) >> 2];
		strEncode+= EncodeTable[((Tmp[1] & 0x03) << 4)];
		strEncode+= "==";
	}
	else if(Mod==2)
	{
		Tmp[1] = *Data++;
		Tmp[2] = *Data++;
		strEncode+= EncodeTable[(Tmp[1] & 0xFC) >> 2];
		strEncode+= EncodeTable[((Tmp[1] & 0x03) << 4) | ((Tmp[2] & 0xF0) >> 4)];
		strEncode+= EncodeTable[((Tmp[2] & 0x0F) << 2)];
		strEncode+= "=";
	}
	return JKConvert::toCharArray(strEncode.c_str() , strEncode.size());
}

vector<char> JKEncryption::Base64Decryptor(vector<char> a_cIn)
{
	char *Data = &a_cIn.at(0);
	int DataByte = a_cIn.size();
	//解码表
	const char DecodeTable[] =
	{
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		62, // '+'
		0, 0, 0,
		63, // '/'
		52, 53, 54, 55, 56, 57, 58, 59, 60, 61, // '0'-'9'
		0, 0, 0, 0, 0, 0, 0,
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
		13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, // 'A'-'Z'
		0, 0, 0, 0, 0, 0,
		26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38,
		39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, // 'a'-'z'
	};
	//返回值
	string strDecode;
	int nValue;
	int i= 0;
	while (i < DataByte)
	{
		if (*Data != '\r' && *Data!='\n')
		{
			nValue = DecodeTable[*Data++] << 18;
			nValue += DecodeTable[*Data++] << 12;
			strDecode+=(nValue & 0x00FF0000) >> 16;
			//OutByte++;
			if (*Data != '=')
			{
				nValue += DecodeTable[*Data++] << 6;
				strDecode+=(nValue & 0x0000FF00) >> 8;
				//OutByte++;
				if (*Data != '=')
				{
					nValue += DecodeTable[*Data++];
					strDecode+=nValue & 0x000000FF;
					//OutByte++;
				}
			}
			i += 4;
		}
		else// 回车换行,跳过
		{
			Data++;
			i++;
		}
	}
	return JKConvert::toCharArray(strDecode , "UTF-8");
}

string JKEncryption::SHA1_40(string tString)
{
	 unsigned char result[20];
	 CSHA1 sha1;
	 sha1.Reset();
	 sha1.Update((UINT_8 *)tString.c_str(), 6);
	 sha1.Final();
	 sha1.GetHash(result);
	 string hexstr;  
	 for (int i=0;i<sizeof(result);i++)  
	 {  
		 char hex1;  
		 char hex2;  
		 int value=result[i]; 
		 int v1=value/16;  
		 int v2=value % 16;   
		 if (v1>=0&&v1<=9)  
			 hex1=(char)(48+v1);  
		 else  
			 hex1=(char)(55+v1);  
		 if (v2>=0&&v2<=9)  
			 hex2=(char)(48+v2);  
		 else  
			 hex2=(char)(55+v2);  
		 hexstr=hexstr+hex1+hex2;  
	 }  
	 return hexstr;
}
