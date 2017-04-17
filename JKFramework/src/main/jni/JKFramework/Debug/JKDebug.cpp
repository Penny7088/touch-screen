#include "JKStdafx.h"

int JKDebug::nDebug = 1;
JavaVM*  JKDebug::gJavaVM = NULL;
bool  JKDebug::bThread = false;

void JKDebug::InitVM(JavaVM * javaVM)
{
	gJavaVM = javaVM;
}

void JKDebug::Init(int nTmp)
{
    JKDebug::nDebug = nTmp;
	if (nTmp != 0)
		JKException::InitCrash();
}

JNIEnv* JKDebug::GetJNIEvent()
{
	if (bThread)
		return NULL;
	JNIEnv* env;
	int nStatus = gJavaVM->GetEnv((void**)&env,JNI_VERSION_1_6);
	if(nStatus < 0)
	{
		nStatus = gJavaVM->AttachCurrentThread(&env, NULL);
		if(nStatus < 0)
		{
			return NULL;
		}
		bThread = true;
	}
	return env;
}

void JKDebug::ReleaseJNIEvent()
{
	if(bThread)
	{
		gJavaVM->DetachCurrentThread();
		bThread = false;
	}
}
