#ifndef JKDEBUG_H_
#define JKDEBUG_H_

#include <jni.h>
#include <pthread.h>


class JKDebug{
public:
	/**Debug版本号(0为发布版)*/
	static int nDebug;
	/**初始JNI虚拟机环境*/
	static void InitVM(JavaVM * javaVM);
	/**初始化版本*/
	static void Init(int nDebug);
	/**获取JNI对象*/
	static JNIEnv* GetJNIEvent();
	/**释放JNI对象*/
	static void ReleaseJNIEvent();
private:
	/**java虚拟机对象*/
	static JavaVM* gJavaVM;
	/**是否需要Attach线程获取JNI*/
	static bool bThread;
};

#endif /* JKDEBUG_H_ */
