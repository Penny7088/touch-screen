#ifndef JKANALYSIS_H_
#define JKANALYSIS_H_

#include <string>
#include <vector>

using namespace std;



class JKAnalysis{
public:
	/**字符串转大写*/
	static string toUpper(string tText);
	/*字符串转小写*/
	static string toLower(string tText);
	/**分割字符串*/
	static vector<string> Split(string tText, string tDelimiter);
	/**批量替换字符串*/
	static string ReplaceAll(string tStr, string tOld, string tNew);
	/**获取字符串中指定字符串区间内容*/
	static string GetMiddleString(string tText, int nPos, string tFirst, string tEnd);
	/**获取字符串中指定字符串区间内容*/
	static string GetMiddleString(string tText, string tFirst, string tEnd,int nOrder);
	/**获取字符串中指定字符串区间内容*/
	static string GetMiddleString(string tText, string tFirst, string tEnd);
};

#endif /* JKANALYSIS_H_ */
