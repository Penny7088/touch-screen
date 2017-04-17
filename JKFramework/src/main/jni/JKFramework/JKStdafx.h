#ifndef JKSTDAFX_H_
#define JKSTDAFX_H_

extern "C"
{
	#include "../3rd/aes/aes.h"
}
#include "../3rd/md5/md5.h"
#include "../3rd/sha1/sha1.h"
#include "Algorithm/JKAnalysis.h"
#include "Algorithm/JKConvert.h"
#include "Algorithm/JKFile.h"
#include "Algorithm/JKDate.h"
#include "Algorithm/JKEncryption.h"
#include "Debug/JKJni.h"
#include "Debug/JKDebug.h"
#include "Debug/JKLog.h"
#include "Debug/JKException.h"

#include <string>
#include <sstream>
#include <algorithm>
#include <cstdlib>
#include <stdlib.h>
#include <stdio.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include <android/log.h>
#include <assert.h>
#include <jni.h>


using namespace std;

#endif /* JKSTDAFX_H_ */
