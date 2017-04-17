#include "JKStdafx.h"


string JKAnalysis::toUpper(string tText)
{
	transform(tText.begin(), tText.end(), tText.begin(), ::toupper);
	return tText;
}

string JKAnalysis::toLower(string tText)
{
	transform(tText.begin(), tText.end(), tText.begin(), ::tolower);
	return tText;
}


vector<string> JKAnalysis::Split(string tText, string tDelimiter) {
	vector<string> result;
	size_t start = 0;
	size_t index = tText.find(tDelimiter);
	while (index != string::npos) {
		string tAdd = tText.substr(start, index - start);
		if (tAdd != "")
			result.push_back(tAdd);
		start = index + tDelimiter.length();
		index = tText.find(tDelimiter, start);
	}
	//加最后一轮
	string tAdd = tText.substr(start, index - start);
	if (tAdd != "")
		result.push_back(tAdd);
	return result;
}

string JKAnalysis::ReplaceAll(string tStr, string tOld, string tNew)
{
	int nPos = 0;
	while (true)
	{
		nPos = tStr.find(tOld,nPos);
		if (nPos < 0)
			break;
		tStr.replace(nPos,tOld.length(),tNew);
		nPos += tNew.length();
	}
	return tStr;
}

string  JKAnalysis::GetMiddleString(string tText, int nPos, string tFirst, string tEnd)
{
	int nStart = tText.find(tFirst , nPos);
	if (nStart < 0)     //内容没有找到
		return "";
	nStart += tFirst.length();

	int nEnd = tText.find(tEnd, nStart + 1);
	if (nEnd < 0)     //内容没有找到
		return "";

	return tText.substr(nStart, nEnd - nStart);
}


string JKAnalysis::GetMiddleString(string tText, string tFirst, string tEnd,int nOrder)
{
	int nStart = -1;      //其实搜索位置
	while (nOrder > 0)
	{
		nStart = tText.find(tFirst, nStart + 1);
		if (nStart < 0)    //内容没有找到
			return "";
		--nOrder;
	}

	return GetMiddleString(tText, nStart, tFirst, tEnd);
}

string JKAnalysis::GetMiddleString(string tText, string tFirst, string tEnd)
{
	return GetMiddleString(tText, 0, tFirst, tEnd);
}
