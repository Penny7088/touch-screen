LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES:= 3rd/md5/md5.cpp \
3rd/sha1/sha1.cpp \
3rd/aes/aes_modes.c \
3rd/aes/aes_ni.c \
3rd/aes/aescrypt.c \
3rd/aes/aeskey.c \
3rd/aes/aestab.c \
JKFramework/Jni/JKNative.cpp \
JKFramework/Debug/JKJni.cpp \
JKFramework/Debug/JKDebug.cpp \
JKFramework/Debug/JKLog.cpp \
JKFramework/Debug/JKException.cpp \
JKFramework/Algorithm/JKFile.cpp \
JKFramework/Algorithm/JKConvert.cpp \
JKFramework/Algorithm/JKAnalysis.cpp \
JKFramework/Algorithm/JKDate.cpp \
JKFramework/Algorithm/JKEncryption.cpp


LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/JKFramework
LOCAL_C_INCLUDES := $(LOCAL_PATH)/JKFramework

LOCAL_MODULE:= JKFramework

LOCAL_MODULE_FILENAME := libJKFramework

LOCAL_LDLIBS:= -lz -llog -landroid

#LOCAL_WHOLE_STATIC_LIBRARIES += android_support

include $(BUILD_SHARED_LIBRARY)

#$(call import-module, android/support)
