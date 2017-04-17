#include "JKStdafx.h"

string JKLog::tDebugtTag = "JKDebug";


void JKLog::d(string tMsg){
	if(0 != JKDebug::nDebug)
		__android_log_write(ANDROID_LOG_DEBUG,JKLog::tDebugtTag.c_str(),tMsg.c_str());
}

void JKLog::e(string tMsg){
	if(0 != JKDebug::nDebug)
		__android_log_write(ANDROID_LOG_ERROR,JKLog::tDebugtTag.c_str(),tMsg.c_str());
}
