#include "JKStdafx.h"

extern "C"{

    JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved) {
        JKDebug::InitVM(jvm);
        return JNI_VERSION_1_6;
    }

    JNIEXPORT void JNICALL Java_com_jkframework_debug_JKDebug_Init(JNIEnv *env, jclass clazz,jint nDebug){
        JKDebug::Init(nDebug);
    }
}
