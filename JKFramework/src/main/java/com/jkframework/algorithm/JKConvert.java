package com.jkframework.algorithm;


import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.util.TypedValue;

import com.jkframework.debug.JKDebug;
import com.jkframework.debug.JKLog;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class JKConvert {
	/** 繁体字库 */
	private static String tTw = "佔歷萬與醜專業叢東絲丟兩嚴喪個爿豐臨為麗舉麼義烏樂喬習鄉書買亂爭於虧雲亙亞產畝親褻嚲億僅從侖倉儀們價眾優夥會傴傘偉傳傷倀倫傖偽佇體餘傭僉俠侶僥偵側僑儈儕儂俁儔儼倆儷儉債傾傯僂僨償儻儐儲儺兒兌兗黨蘭關興茲養獸囅內岡冊寫軍農塚馮衝決況凍淨淒涼淩減湊凜幾鳳鳧憑凱擊氹鑿芻劃劉則剛創刪別剗剄劊劌剴劑剮劍剝劇勸辦務勱動勵勁勞勢勳猛勩勻匭匱區醫華協單賣盧鹵臥衛卻巹廠廳曆厲壓厭厙廁廂厴廈廚廄廝縣參靉靆雙發變敘疊葉號歎嘰籲後嚇呂嗎唚噸聽啟吳嘸囈嘔嚦唄員咼嗆嗚詠哢嚨嚀噝吒噅鹹呱響啞噠嘵嗶噦嘩噲嚌噥喲嘜嗊嘮啢嗩唕喚呼嘖嗇囀齧囉嘽嘯噴嘍嚳囁嗬噯噓嚶囑嚕劈囂謔團園囪圍圇國圖圓聖壙場阪壞塊堅壇壢壩塢墳墜壟壟壚壘墾坰堊墊埡墶壋塏堖塒塤堝墊垵塹墮壪牆壯聲殼壺壼處備複夠頭誇夾奪奩奐奮獎奧妝婦媽嫵嫗媯姍薑婁婭嬈嬌孌娛媧嫻嫿嬰嬋嬸媼嬡嬪嬙嬤孫學孿寧寶實寵審憲宮寬賓寢對尋導壽將爾塵堯尷屍盡層屭屜屆屬屢屨嶼歲豈嶇崗峴嶴嵐島嶺嶽崠巋嶨嶧峽嶢嶠崢巒嶗崍嶮嶄嶸嶔崳嶁脊巔鞏巰幣帥師幃帳簾幟帶幀幫幬幘幗冪襆幹並廣莊慶廬廡庫應廟龐廢廎廩開異棄張彌弳彎彈強歸當錄彠彥徹徑徠禦憶懺憂愾懷態慫憮慪悵愴憐總懟懌戀懇惡慟懨愷惻惱惲悅愨懸慳憫驚懼慘懲憊愜慚憚慣湣慍憤憒願懾憖怵懣懶懍戇戔戲戧戰戩戶紮撲扡執擴捫掃揚擾撫拋摶摳掄搶護報擔擬攏揀擁攔擰撥擇掛摯攣掗撾撻挾撓擋撟掙擠揮撏撈損撿換搗據撚擄摑擲撣摻摜摣攬撳攙擱摟攪攜攝攄擺搖擯攤攖撐攆擷擼攛擻攢敵斂數齋斕鬥斬斷無舊時曠暘曇晝曨顯晉曬曉曄暈暉暫曖劄術樸機殺雜權條來楊榪傑極構樅樞棗櫪梘棖槍楓梟櫃檸檉梔柵標棧櫛櫳棟櫨櫟欄樹棲樣欒棬椏橈楨檔榿橋樺檜槳樁夢檮棶檢欞槨櫝槧欏橢樓欖櫬櫚櫸檟檻檳櫧橫檣櫻櫫櫥櫓櫞簷檁歡歟歐殲歿殤殘殞殮殫殯毆毀轂畢斃氈毿氌氣氫氬氳彙漢汙湯洶遝溝沒灃漚瀝淪滄渢溈滬濔濘淚澩瀧瀘濼瀉潑澤涇潔灑窪浹淺漿澆湞溮濁測澮濟瀏滻渾滸濃潯濜塗湧濤澇淶漣潿渦溳渙滌潤澗漲澀澱淵淥漬瀆漸澠漁瀋滲溫遊灣濕潰濺漵漊潷滾滯灩灄滿瀅濾濫灤濱灘澦濫瀠瀟瀲濰潛瀦瀾瀨瀕灝滅燈靈災燦煬爐燉煒熗點煉熾爍爛烴燭煙煩燒燁燴燙燼熱煥燜燾煆糊溜愛爺牘犛牽犧犢強狀獷獁猶狽麅獮獰獨狹獅獪猙獄猻獫獵獼玀豬貓蝟獻獺璣璵瑒瑪瑋環現瑲璽瑉玨琺瓏璫琿璡璉瑣瓊瑤璦璿瓔瓚甕甌電畫暢佘疇癤療瘧癘瘍鬁瘡瘋皰屙癰痙癢瘂癆瘓癇癡癉瘮瘞瘺癟癱癮癭癩癬癲臒皚皺皸盞鹽監蓋盜盤瞘眥矓睜睞瞼瞞矚矯磯礬礦碭碼磚硨硯碸礪礱礫礎硜矽碩硤磽磑礄確鹼礙磧磣堿镟滾禮禕禰禎禱禍稟祿禪離禿稈種積稱穢穠穭稅穌穩穡窮竊竅窯竄窩窺竇窶豎競篤筍筆筧箋籠籩築篳篩簹箏籌簽簡籙簀篋籜籮簞簫簣簍籃籬籪籟糴類秈糶糲粵糞糧糝餱緊縶糸糾紆紅紂纖紇約級紈纊紀紉緯紜紘純紕紗綱納紝縱綸紛紙紋紡紵紖紐紓線紺絏紱練組紳細織終縐絆紼絀紹繹經紿綁絨結絝繞絰絎繪給絢絳絡絕絞統綆綃絹繡綌綏絛繼綈績緒綾緓續綺緋綽緔緄繩維綿綬繃綢綯綹綣綜綻綰綠綴緇緙緗緘緬纜緹緲緝縕繢緦綞緞緶線緱縋緩締縷編緡緣縉縛縟縝縫縗縞纏縭縊縑繽縹縵縲纓縮繆繅纈繚繕繒韁繾繰繯繳纘罌網羅罰罷羆羈羥羨翹翽翬耮耬聳恥聶聾職聹聯聵聰肅腸膚膁腎腫脹脅膽勝朧腖臚脛膠脈膾髒臍腦膿臠腳脫腡臉臘醃膕齶膩靦膃騰臏臢輿艤艦艙艫艱豔艸藝節羋薌蕪蘆蓯葦藶莧萇蒼苧蘇檾蘋莖蘢蔦塋煢繭荊薦薘莢蕘蓽蕎薈薺蕩榮葷滎犖熒蕁藎蓀蔭蕒葒葤藥蒞蓧萊蓮蒔萵薟獲蕕瑩鶯蓴蘀蘿螢營縈蕭薩蔥蕆蕢蔣蔞藍薊蘺蕷鎣驀薔蘞藺藹蘄蘊藪槁蘚虜慮虛蟲虯蟣雖蝦蠆蝕蟻螞蠶蠔蜆蠱蠣蟶蠻蟄蛺蟯螄蠐蛻蝸蠟蠅蟈蟬蠍螻蠑螿蟎蠨釁銜補襯袞襖嫋褘襪襲襏裝襠褌褳襝褲襇褸襤繈襴見觀覎規覓視覘覽覺覬覡覿覥覦覯覲覷觴觸觶讋譽謄訁計訂訃認譏訐訌討讓訕訖訓議訊記訒講諱謳詎訝訥許訛論訩訟諷設訪訣證詁訶評詛識詗詐訴診詆謅詞詘詔詖譯詒誆誄試詿詩詰詼誠誅詵話誕詬詮詭詢詣諍該詳詫諢詡譸誡誣語誚誤誥誘誨誑說誦誒請諸諏諾讀諑誹課諉諛誰諗調諂諒諄誶談誼謀諶諜謊諫諧謔謁謂諤諭諼讒諮諳諺諦謎諞諝謨讜謖謝謠謗諡謙謐謹謾謫譾謬譚譖譙讕譜譎讞譴譫讖穀豶貝貞負貟貢財責賢敗賬貨質販貪貧貶購貯貫貳賤賁貰貼貴貺貸貿費賀貽賊贄賈賄貲賃賂贓資賅贐賕賑賚賒賦賭齎贖賞賜贔賙賡賠賧賴賵贅賻賺賽賾贗讚贇贈贍贏贛赬趙趕趨趲躉躍蹌蹠躒踐躂蹺蹕躚躋踴躊蹤躓躑躡蹣躕躥躪躦軀車軋軌軒軑軔轉軛輪軟轟軲軻轤軸軹軼軤軫轢軺輕軾載輊轎輈輇輅較輒輔輛輦輩輝輥輞輬輟輜輳輻輯轀輸轡轅轄輾轆轍轔辭辯辮邊遼達遷過邁運還這進遠違連遲邇逕跡適選遜遞邐邏遺遙鄧鄺鄔郵鄒鄴鄰鬱郤郟鄶鄭鄆酈鄖鄲醞醱醬釅釃釀釋裏钜鑒鑾鏨釓釔針釘釗釙釕釷釺釧釤鈒釩釣鍆釹鍚釵鈃鈣鈈鈦鈍鈔鍾鈉鋇鋼鈑鈐鑰欽鈞鎢鉤鈧鈁鈥鈄鈕鈀鈺錢鉦鉗鈷缽鈳鉕鈽鈸鉞鑽鉬鉭鉀鈿鈾鐵鉑鈴鑠鉛鉚鈰鉉鉈鉍鈹鐸鉶銬銠鉺銪鋏鋣鐃銍鐺銅鋁銱銦鎧鍘銖銑鋌銩銛鏵銓鉿銚鉻銘錚銫鉸銥鏟銃鐋銨銀銣鑄鐒鋪鋙錸鋱鏈鏗銷鎖鋰鋥鋤鍋鋯鋨鏽銼鋝鋒鋅鋶鐦鐧銳銻鋃鋟鋦錒錆鍺錯錨錡錁錕錩錫錮鑼錘錐錦鍁錈錇錟錠鍵鋸錳錙鍥鍈鍇鏘鍶鍔鍤鍬鍛鎪鍠鍰鎄鍍鎂鏤鎡鏌鎮鎛鎘鑷鐫鎳鎿鎦鎬鎊鎰鎔鏢鏜鏍鏰鏞鏡鏑鏃鏇鏐鐔钁鐐鏷鑥鐓鑭鐠鑹鏹鐙鑊鐳鐶鐲鐮鐿鑔鑣鑞鑲長門閂閃閆閈閉問闖閏闈閎間閔閌悶閘鬧閨聞闥閩閭闓閥閣閡閫鬮閱閬闍閾閹閶鬩閿閽閻閼闡闌闃闠闊闋闔闐闒闕闞闤隊陽陰陣階際陸隴陳陘陝隉隕險隨隱隸雋難雛讎靂霧霽黴靄靚靜靨韃鞽韉韝韋韌韍韓韙韞韜韻頁頂頃頇項順須頊頑顧頓頎頒頌頏預顱領頗頸頡頰頲頜潁熲頦頤頻頮頹頷頴穎顆題顒顎顓顏額顳顢顛顙顥纇顫顬顰顴風颺颭颮颯颶颸颼颻飀飄飆飆飛饗饜飣饑飥餳飩餼飪飫飭飯飲餞飾飽飼飿飴餌饒餉餄餎餃餏餅餑餖餓餘餒餕餜餛餡館餷饋餶餿饞饁饃餺餾饈饉饅饊饌饢馬馭馱馴馳驅馹駁驢駔駛駟駙駒騶駐駝駑駕驛駘驍罵駰驕驊駱駭駢驫驪騁驗騂駸駿騏騎騍騅騌驌驂騙騭騤騷騖驁騮騫騸驃騾驄驏驟驥驦驤髏髖髕鬢魘魎魚魛魢魷魨魯魴魺鮁鮃鯰鱸鮋鮓鮒鮊鮑鱟鮍鮐鮭鮚鮳鮪鮞鮦鰂鮜鱠鱭鮫鮮鮺鯗鱘鯁鱺鰱鰹鯉鰣鰷鯀鯊鯇鮶鯽鯒鯖鯪鯕鯫鯡鯤鯧鯝鯢鯰鯛鯨鯵鯴鯔鱝鰈鰏鱨鯷鰮鰃鰓鱷鰍鰒鰉鰁鱂鯿鰠鼇鰭鰨鰥鰩鰟鰜鰳鰾鱈鱉鰻鰵鱅鰼鱖鱔鱗鱒鱯鱤鱧鱣鳥鳩雞鳶鳴鳲鷗鴉鶬鴇鴆鴣鶇鸕鴨鴞鴦鴒鴟鴝鴛鴬鴕鷥鷙鴯鴰鵂鴴鵃鴿鸞鴻鵐鵓鸝鵑鵠鵝鵒鷳鵜鵡鵲鶓鵪鶤鵯鵬鵮鶉鶊鵷鷫鶘鶡鶚鶻鶿鶥鶩鷊鷂鶲鶹鶺鷁鶼鶴鷖鸚鷓鷚鷯鷦鷲鷸鷺鸇鷹鸌鸏鸛鸘鹺麥麩黃黌黶黷黲黽黿鼂鼉鞀鼴齇齊齏齒齔齕齗齟齡齙齠齜齦齬齪齲齷龍龔龕龜誌製谘隻係範鬆冇嚐嘗鬨麵準鐘彆閒儘臟拚註臺";
	/** 简体字库 */
	private static String tCn = "占历万与丑专业丛东丝丢两严丧个丬丰临为丽举么义乌乐乔习乡书买乱争于亏云亘亚产亩亲亵亸亿仅从仑仓仪们价众优伙会伛伞伟传伤伥伦伧伪伫体余佣佥侠侣侥侦侧侨侩侪侬俣俦俨俩俪俭债倾偬偻偾偿傥傧储傩儿兑兖党兰关兴兹养兽冁内冈册写军农冢冯冲决况冻净凄凉凌减凑凛几凤凫凭凯击凼凿刍划刘则刚创删别刬刭刽刿剀剂剐剑剥剧劝办务劢动励劲劳势勋勐勚匀匦匮区医华协单卖卢卤卧卫却卺厂厅历厉压厌厍厕厢厣厦厨厩厮县参叆叇双发变叙叠叶号叹叽吁后吓吕吗吣吨听启吴呒呓呕呖呗员呙呛呜咏咔咙咛咝咤咴咸哌响哑哒哓哔哕哗哙哜哝哟唛唝唠唡唢唣唤唿啧啬啭啮啰啴啸喷喽喾嗫呵嗳嘘嘤嘱噜噼嚣嚯团园囱围囵国图圆圣圹场坂坏块坚坛坜坝坞坟坠垄垅垆垒垦垧垩垫垭垯垱垲垴埘埙埚埝埯堑堕塆墙壮声壳壶壸处备复够头夸夹夺奁奂奋奖奥妆妇妈妩妪妫姗姜娄娅娆娇娈娱娲娴婳婴婵婶媪嫒嫔嫱嬷孙学孪宁宝实宠审宪宫宽宾寝对寻导寿将尔尘尧尴尸尽层屃屉届属屡屦屿岁岂岖岗岘岙岚岛岭岳岽岿峃峄峡峣峤峥峦崂崃崄崭嵘嵚嵛嵝嵴巅巩巯币帅师帏帐帘帜带帧帮帱帻帼幂幞干并广庄庆庐庑库应庙庞废庼廪开异弃张弥弪弯弹强归当录彟彦彻径徕御忆忏忧忾怀态怂怃怄怅怆怜总怼怿恋恳恶恸恹恺恻恼恽悦悫悬悭悯惊惧惨惩惫惬惭惮惯愍愠愤愦愿慑慭憷懑懒懔戆戋戏戗战戬户扎扑扦执扩扪扫扬扰抚抛抟抠抡抢护报担拟拢拣拥拦拧拨择挂挚挛挜挝挞挟挠挡挢挣挤挥挦捞损捡换捣据捻掳掴掷掸掺掼揸揽揿搀搁搂搅携摄摅摆摇摈摊撄撑撵撷撸撺擞攒敌敛数斋斓斗斩断无旧时旷旸昙昼昽显晋晒晓晔晕晖暂暧札术朴机杀杂权条来杨杩杰极构枞枢枣枥枧枨枪枫枭柜柠柽栀栅标栈栉栊栋栌栎栏树栖样栾桊桠桡桢档桤桥桦桧桨桩梦梼梾检棂椁椟椠椤椭楼榄榇榈榉槚槛槟槠横樯樱橥橱橹橼檐檩欢欤欧歼殁殇残殒殓殚殡殴毁毂毕毙毡毵氇气氢氩氲汇汉污汤汹沓沟没沣沤沥沦沧沨沩沪沵泞泪泶泷泸泺泻泼泽泾洁洒洼浃浅浆浇浈浉浊测浍济浏浐浑浒浓浔浕涂涌涛涝涞涟涠涡涢涣涤润涧涨涩淀渊渌渍渎渐渑渔渖渗温游湾湿溃溅溆溇滗滚滞滟滠满滢滤滥滦滨滩滪漤潆潇潋潍潜潴澜濑濒灏灭灯灵灾灿炀炉炖炜炝点炼炽烁烂烃烛烟烦烧烨烩烫烬热焕焖焘煅煳熘爱爷牍牦牵牺犊犟状犷犸犹狈狍狝狞独狭狮狯狰狱狲猃猎猕猡猪猫猬献獭玑玙玚玛玮环现玱玺珉珏珐珑珰珲琎琏琐琼瑶瑷璇璎瓒瓮瓯电画畅畲畴疖疗疟疠疡疬疮疯疱疴痈痉痒痖痨痪痫痴瘅瘆瘗瘘瘪瘫瘾瘿癞癣癫癯皑皱皲盏盐监盖盗盘眍眦眬睁睐睑瞒瞩矫矶矾矿砀码砖砗砚砜砺砻砾础硁硅硕硖硗硙硚确硷碍碛碜碱碹磙礼祎祢祯祷祸禀禄禅离秃秆种积称秽秾稆税稣稳穑穷窃窍窑窜窝窥窦窭竖竞笃笋笔笕笺笼笾筑筚筛筜筝筹签简箓箦箧箨箩箪箫篑篓篮篱簖籁籴类籼粜粝粤粪粮糁糇紧絷纟纠纡红纣纤纥约级纨纩纪纫纬纭纮纯纰纱纲纳纴纵纶纷纸纹纺纻纼纽纾线绀绁绂练组绅细织终绉绊绋绌绍绎经绐绑绒结绔绕绖绗绘给绚绛络绝绞统绠绡绢绣绤绥绦继绨绩绪绫绬续绮绯绰绱绲绳维绵绶绷绸绹绺绻综绽绾绿缀缁缂缃缄缅缆缇缈缉缊缋缌缍缎缏缐缑缒缓缔缕编缗缘缙缚缛缜缝缞缟缠缡缢缣缤缥缦缧缨缩缪缫缬缭缮缯缰缱缲缳缴缵罂网罗罚罢罴羁羟羡翘翙翚耢耧耸耻聂聋职聍联聩聪肃肠肤肷肾肿胀胁胆胜胧胨胪胫胶脉脍脏脐脑脓脔脚脱脶脸腊腌腘腭腻腼腽腾膑臜舆舣舰舱舻艰艳艹艺节芈芗芜芦苁苇苈苋苌苍苎苏苘苹茎茏茑茔茕茧荆荐荙荚荛荜荞荟荠荡荣荤荥荦荧荨荩荪荫荬荭荮药莅莜莱莲莳莴莶获莸莹莺莼萚萝萤营萦萧萨葱蒇蒉蒋蒌蓝蓟蓠蓣蓥蓦蔷蔹蔺蔼蕲蕴薮藁藓虏虑虚虫虬虮虽虾虿蚀蚁蚂蚕蚝蚬蛊蛎蛏蛮蛰蛱蛲蛳蛴蜕蜗蜡蝇蝈蝉蝎蝼蝾螀螨蟏衅衔补衬衮袄袅袆袜袭袯装裆裈裢裣裤裥褛褴襁襕见观觃规觅视觇览觉觊觋觌觍觎觏觐觑觞触觯詟誉誊讠计订讣认讥讦讧讨让讪讫训议讯记讱讲讳讴讵讶讷许讹论讻讼讽设访诀证诂诃评诅识诇诈诉诊诋诌词诎诏诐译诒诓诔试诖诗诘诙诚诛诜话诞诟诠诡询诣诤该详诧诨诩诪诫诬语诮误诰诱诲诳说诵诶请诸诹诺读诼诽课诿谀谁谂调谄谅谆谇谈谊谋谌谍谎谏谐谑谒谓谔谕谖谗谘谙谚谛谜谝谞谟谠谡谢谣谤谥谦谧谨谩谪谫谬谭谮谯谰谱谲谳谴谵谶谷豮贝贞负贠贡财责贤败账货质贩贪贫贬购贮贯贰贱贲贳贴贵贶贷贸费贺贻贼贽贾贿赀赁赂赃资赅赆赇赈赉赊赋赌赍赎赏赐赑赒赓赔赕赖赗赘赙赚赛赜赝赞赟赠赡赢赣赪赵赶趋趱趸跃跄跖跞践跶跷跸跹跻踊踌踪踬踯蹑蹒蹰蹿躏躜躯车轧轨轩轪轫转轭轮软轰轱轲轳轴轵轶轷轸轹轺轻轼载轾轿辀辁辂较辄辅辆辇辈辉辊辋辌辍辎辏辐辑辒输辔辕辖辗辘辙辚辞辩辫边辽达迁过迈运还这进远违连迟迩迳迹适选逊递逦逻遗遥邓邝邬邮邹邺邻郁郄郏郐郑郓郦郧郸酝酦酱酽酾酿释里鉅鉴銮錾钆钇针钉钊钋钌钍钎钏钐钑钒钓钔钕钖钗钘钙钚钛钝钞钟钠钡钢钣钤钥钦钧钨钩钪钫钬钭钮钯钰钱钲钳钴钵钶钷钸钹钺钻钼钽钾钿铀铁铂铃铄铅铆铈铉铊铋铍铎铏铐铑铒铕铗铘铙铚铛铜铝铞铟铠铡铢铣铤铥铦铧铨铪铫铬铭铮铯铰铱铲铳铴铵银铷铸铹铺铻铼铽链铿销锁锂锃锄锅锆锇锈锉锊锋锌锍锎锏锐锑锒锓锔锕锖锗错锚锜锞锟锠锡锢锣锤锥锦锨锩锫锬锭键锯锰锱锲锳锴锵锶锷锸锹锻锼锽锾锿镀镁镂镃镆镇镈镉镊镌镍镎镏镐镑镒镕镖镗镙镚镛镜镝镞镟镠镡镢镣镤镥镦镧镨镩镪镫镬镭镮镯镰镱镲镳镴镶长门闩闪闫闬闭问闯闰闱闳间闵闶闷闸闹闺闻闼闽闾闿阀阁阂阃阄阅阆阇阈阉阊阋阌阍阎阏阐阑阒阓阔阕阖阗阘阙阚阛队阳阴阵阶际陆陇陈陉陕陧陨险随隐隶隽难雏雠雳雾霁霉霭靓静靥鞑鞒鞯鞴韦韧韨韩韪韫韬韵页顶顷顸项顺须顼顽顾顿颀颁颂颃预颅领颇颈颉颊颋颌颍颎颏颐频颒颓颔颕颖颗题颙颚颛颜额颞颟颠颡颢颣颤颥颦颧风飏飐飑飒飓飔飕飖飗飘飙飚飞飨餍饤饥饦饧饨饩饪饫饬饭饮饯饰饱饲饳饴饵饶饷饸饹饺饻饼饽饾饿馀馁馂馃馄馅馆馇馈馉馊馋馌馍馎馏馐馑馒馓馔馕马驭驮驯驰驱驲驳驴驵驶驷驸驹驺驻驼驽驾驿骀骁骂骃骄骅骆骇骈骉骊骋验骍骎骏骐骑骒骓骔骕骖骗骘骙骚骛骜骝骞骟骠骡骢骣骤骥骦骧髅髋髌鬓魇魉鱼鱽鱾鱿鲀鲁鲂鲄鲅鲆鲇鲈鲉鲊鲋鲌鲍鲎鲏鲐鲑鲒鲓鲔鲕鲖鲗鲘鲙鲚鲛鲜鲝鲞鲟鲠鲡鲢鲣鲤鲥鲦鲧鲨鲩鲪鲫鲬鲭鲮鲯鲰鲱鲲鲳鲴鲵鲶鲷鲸鲹鲺鲻鲼鲽鲾鲿鳀鳁鳂鳃鳄鳅鳆鳇鳈鳉鳊鳋鳌鳍鳎鳏鳐鳑鳒鳓鳔鳕鳖鳗鳘鳙鳛鳜鳝鳞鳟鳠鳡鳢鳣鸟鸠鸡鸢鸣鸤鸥鸦鸧鸨鸩鸪鸫鸬鸭鸮鸯鸰鸱鸲鸳鸴鸵鸶鸷鸸鸹鸺鸻鸼鸽鸾鸿鹀鹁鹂鹃鹄鹅鹆鹇鹈鹉鹊鹋鹌鹍鹎鹏鹐鹑鹒鹓鹔鹕鹖鹗鹘鹚鹛鹜鹝鹞鹟鹠鹡鹢鹣鹤鹥鹦鹧鹨鹩鹪鹫鹬鹭鹯鹰鹱鹲鹳鹴鹾麦麸黄黉黡黩黪黾鼋鼌鼍鼗鼹齄齐齑齿龀龁龂龃龄龅龆龇龈龉龊龋龌龙龚龛龟志制咨只系范松没尝尝闹面准钟别闲尽脏拼注台";
	/** 繁转简的繁体词库 */
	private static String[] a_tTwBefore = { "髮", "鑑", "裡", "閑", "麽", "総", "牠", "譁", "反覆", "菸",
			"乾革命", "印像", "抽像", "巳经", "对像", "萝卜乾", "乾粮", "駡", "昇", "着作", "编着", "　着", "十起", "名着",
			"论着", "专着", "执着", "原着", "着述", "遗着", "着：", "卓着", "瞭解", "着称", "接著，", "着称", "着有", "着者",
			"眞", "爲", "乾枯", "着」", "僞", "着》", "慾", "甚麼", "阖", "扞卫", "乾旱", "着，", "着：", "寃", "羣",
			"存摺", "衆", "崑", "衞", "鍚", "枱", "慂", "监别", "监證", "綫", "脗", "竪", "賸", "隣", "蹟", "獃",
			"乾瞪眼", "乾刷", "鑚", "监别", "监定", "乾涸", "乾木頭", "乾女", "乾妈", "吹乾", "乾尸", "乾冰", "鱼乾", "乾鱼",
			"乾裂", "乾涩", "乾呕", "乾柴", "鎚", "风乾", "外强中乾", "乾渴", "肉乾", "覩", "甯", "乾净", "乾活", "藉机",
			"乾瘪", "史着", "藉铿", "现像", "乾果", "乾什么", "着名", "盃", "徵", "计画", "擧", "綑", "廸", "揑", "粧",
			"敍", "乾漆", "乾膠", "彷佛", "肉乾", "晾乾", "口乾舌燥", "乾硬", "癒", "像徵", "形像", "砲", "显着", "巨着",
			"着《", "着』", "着录", "着；", "峯", "乾吼", "乾儿", "浄", "萤幕", "杈力", "高乾", "乾脆", "所着", "新着", "甚么",
			"舖", "着于", "锺", "曬乾", "着。", "鷄", "牀", "乳臭未乾", "文皺皺", "乾爹", "烤乾", "着文", "饼乾", "彔",
			"乾乾淨淨", "嚐", "儍", "朶", "吸乾", "外強中乾", "乾燥", "监辨", "蒐", "榨乾", "擦乾", "一乾二净", "乾咳", "乾冬",
			"一乾二淨", "乾笑", "乾掃", "乾干净净" };
	/** 繁转简的简体词库 */
	private static String[] a_tCnAfter = { "发", "鉴", "里", "闲", "么", "总", "它", "哗", "反复", "烟",
			"干革命", "印象", "抽象", "已经", "对象", "萝卜干", "干粮", "骂", "升", "著作", "编著", "　著", "拾起", "名著",
			"论著", "专著", "执著", "原著", "著述", "遗著", "著：", "卓著", "了解", "著称", "接着，", "著称", "著有", "著者",
			"真", "为", "干枯", "著」", "伪", "著》", "欲", "什么", "合", "捍卫", "干旱", "著，", "著：", "冤", "群",
			"存折", "众", "崐", "卫", "锡", "台", "涌", "鉴别", "鉴证", "线", "吻", "竖", "剩", "邻", "迹", "呆",
			"干瞪眼", "干刷", "钻", "鉴别", "鉴定", "干涸", "干木头", "干女", "干妈", "吹干", "干尸", "干冰", "鱼干", "干鱼",
			"干裂", "干涩", "干呕", "干柴", "锤", "风干", "外强中干", "干渴", "肉干", "睹", "宁", "干净", "干活", "借机",
			"干瘪", "史著", "借鉴", "现象", "干果", "干什么", "著名", "杯", "征", "计划", "举", "捆", "迪", "捏", "妆",
			"叙", "干漆", "干胶", "仿佛", "肉干", "晾干", "口干舌燥", "干硬", "愈", "象征", "形象", "炮", "显著", "巨著",
			"著《", "著』", "著录", "著；", "峰", "干吼", "干儿", "净", "荧幕", "权力", "高干", "干脆", "所著", "新著", "什么",
			"铺", "著于", "钟", "晒干", "著。", "鸡", "床", "乳臭未干", "文绉绉", "干爹", "烤干", "著文", "饼干", "录",
			"干干净净", "尝", "傻", "朵", "吸干", "外强中干", "干燥", "鉴辨", "搜", "榨干", "擦干", "一干二净", "干咳", "干冬",
			"一干二净", "干笑", "干扫", "干干净净" };
	/** 简转繁的简体词库 */
	private static String[] a_tCnBefore = { "锺", "乾什麽", "幹果", "對像", "註意", "下麵", "瞀衛", "鹹陽",
			"亞裏士多德", "甘迺迪", "西曆", "紐西蘭", "計畫", "甯", "抽像", "印像", "藉鏗", "週總理", "週恩來", "汙", "貪官汙吏",
			"蝟", "劄", "恢複", "關系", "錶明", "代錶", "嶽州", "西元", "±", "專案", "母音", "畢卡索", "首碼", "舂", "乾什麼",
			"裏根", "臺灣", "臺北", "了解", "發根", "裏脊肉", "幹凈", "居裏夫人", "漓江", "墻", "幹旱", "余生", "仿佛", "萝卜",
			"裏程", "头發", "白發", "幹燥", "施舍", "秀發", "鬓發", "發梢", "一裏", "三裏", "五裏", "六裏", "八裏", "十裏",
			"千裏", "万裏", "報道", "鬆廬", "告餘", "神採", "渖阳", "詩雲", "人雲亦雲", "復雜", "復寫紙", "復製", "繁復", "復音詞",
			"懷表", "手表", "脫表", "對表", "塊表", "喂食", "喂養", "乾硬", "為瞭解決", "幹糧", "蘿蔔幹", "想象", "高乾", "藉機",
			"乾活", "巳經", "鹹昌", "鹹寧", "亞裏斯多德", "布希", "計畫", "百性", "網路", "現像", "週総理", "貪汙", "杈力", "睏難",
			"乾活", "乾革命", "員警", "幵", "耶誕節", "去氧核糖核酸", "尾碼", "括弧", "遞迴", "裏奇", "皇後", "臺南", "臺中",
			"裏昂", "沖動", "這么", "那么", "幹幹凈凈", "裏平斯基", "哈裏路亚", "象征", "幹枯", "采取", "巖石", "枯幹", "鉆",
			"吸幹", "采", "復葉", "電子表", "特征", "四裏", "向往", "象征", "發饰", "毛發", "千裏", "復姓", "秒表", "喂飯",
			"發廊", "發丝", "發式", "二裏", "七裏", "鬆井", "喂飽", "裏路", "九裏", "據雲", "復寫", "鐘表", "乾渴", "百裏" };
	/** 简转繁的繁体词库 */
	private static String[] a_tTwAfter = { "鍾", "幹什麽", "乾果", "對象", "注意", "下面", "警衛", "咸陽", "亞里士多德",
			"肯尼迪", "公曆", "新西蘭", "計劃", "寧", "抽象", "印象", "借鑒", "周總理", "周恩來", "污", "貪官污吏", "猬", "札",
			"恢復", "關係", "表明", "代表", "岳州", "公元", "士", "項目", "元音", "畢加索", "前綴", "春", "幹什麼", "里根",
			"台灣", "台北", "瞭解", "髮根", "里脊肉", "乾淨", "居里夫人", "灕江", "牆", "乾旱", "餘生", "彷彿", "蘿蔔", "里程",
			"頭髮", "白髮", "乾燥", "施捨", "秀髮", "鬢髮", "髮梢", "一里", "三里", "五里", "六里", "八里", "十里", "千里",
			"万里", "報導", "鬆廬", "告余", "神采", "沈阳", "詩云", "人云亦云", "複雜", "複寫紙", "複製", "繁複", "複音詞", "懷錶",
			"手錶", "脫錶", "對錶", "塊錶", "餵食", "餵養", "干硬", "為了解決", "乾糧", "蘿蔔乾", "想像", "高幹", "借機", "幹活",
			"已經", "咸昌", "咸寧", "亞里士多德", "布什", "計劃", "百姓", "網絡", "現象", "周総理", "貪污", "權力", "困難", "幹活",
			"幹革命", "警察", "開", "聖誕節", "脫氧核糖核酸", "后綴", "括號", "遞歸", "里奇", "皇后", "台南", "台中", "里昂",
			"衝動", "這麽", "那麽", "乾乾淨淨", "里平斯基", "哈里路亞", "象徵", "乾枯", "採取", "岩石", "枯乾", "鑚", "吸乾", "採",
			"複葉", "電子錶", "特徵", "四里", "嚮往", "象徵", "髮飾", "毛髮", "千里", "複姓", "秒錶", "餵飯", "髮廊", "髮絲",
			"髮式", "二里", "七里", "松井", "餵飽", "里路", "九里", "據云", "複寫", "鐘錶", "干渴", "百里" };

    /**
	 * 将字符串转换为整型
	 * 
	 * @param tText
	 *            转换的字符串
	 * @return 转换后的整型,若失败则转换为0
	 */
	public static int toInt(String tText) {
		try {
            return Integer.parseInt(tText);
		} catch (Exception e) {
			try {
				double dTmp = Double.parseDouble(tText);
				return (int) dTmp;
			} catch (Exception e1) {
				JKLog.ErrorLog("无法将字符串\"" + tText + "\"转成整型.原因为" + e1.getMessage());
				return 0;
			}
		}
	}

	/**
	 * 将boolean变量转为整型
	 * @param bValue boolean变量
	 * @return 真返回1,否则返回0
	 */
	public static int toInt(boolean bValue){
		return bValue ? 1 : 0;
	}

	/**
	 * 将字符串转换为整型
	 * 
	 * @param tText
	 *            转换的字符串
	 * @return 转换后的整型,若失败则转换为0
	 */
	public static long toLong(String tText) {
		try {
            return Long.parseLong(tText);
		} catch (Exception e) {
            try {
                double dTmp = Double.parseDouble(tText);
                return (long) dTmp;
            } catch (Exception e1) {
                JKLog.ErrorLog("无法将字符串\"" + tText + "\"转成长整型.原因为" + e1.getMessage());
                return 0;
            }
        }
	}

	/**
	 * 将字符串转换为单精度浮点型
	 * 
	 * @param tText
	 *            转换的字符串
	 * @return 转换后的单精度浮点型,若失败则转换为0.0f
	 */
	public static float toFloat(String tText) {
		try {
			return Float.parseFloat(tText);
		} catch (Exception e) {
			JKLog.ErrorLog("无法将字符串\"" + tText + "\"转成浮点型.原因为" + e.getMessage());
			return 0.0f;
		}
	}

	/**
	 * 将字符串转换为双精度浮点型
	 * 
	 * @param tText
	 *            转换的字符串
	 * @return 转换后的单精度浮点型,若失败则转换为0.0d
	 */
	public static double toDouble(String tText) {
		try {
			return Double.parseDouble(tText);
		} catch (Exception e) {
			JKLog.ErrorLog("无法将字符串\"" + tText + "\"转成双精度浮点型.原因为" + e.getMessage());
			return 0.0d;
		}
	}

	/**
	 * 将浮点型转换为字符串
	 * 
	 * @param fNum
	 *            转换的浮点型
	 * @return 转换后的字符串
	 */
	public static String toString(float fNum) {
		if ((int)fNum == fNum)
			return Integer.toString((int)fNum);
		else
			return Float.toString(fNum);
	}

	/**
	 * 将双精度浮点数转换为字符串
	 * 
	 * @param dNum
	 *            转换的浮点数
	 * @return 转换后的字符串
	 */
	public static String toString(double dNum) {
		if ((int)dNum == dNum)
			return Integer.toString((int)dNum);
		else
			return Double.toString(dNum);
	}

	/**
	 * 将整形转换为字符串
	 * 
	 * @param nNum
	 *            转换的整形
	 * @return 转换后的字符串
	 */
	public static String toString(int nNum) {
		return "" + nNum;
	}

	/**
	 * 将长整形转换为字符串
	 * 
	 * @param lNum
	 *            转换的长整形
	 * @return 转换后的字符串
	 */
	public static String toString(long lNum) {
		return "" + lNum;
	}

	/**
	 * 将字节数组转换为字符串(默认UTF-8)
	 * 
	 * @param a_byList
	 *            字节数组
	 * @return 转换后的字符串
	 */
	public static String toString(byte[] a_byList) {
		if (a_byList == null)
			return "";
		try {
			return new String(a_byList,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 将字节数组转换为字符串
	 * 
	 * @param a_byList
	 *            字节数组
	 * @param tEncoding
	 *            字节编码(UTF-8,ISO-8859-1,GBK)
	 * @return 转换后的字符串
	 */
	public static String toString(byte[] a_byList, String tEncoding) {
		try {
			return new String(a_byList,tEncoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 取Resource里的string
	 * 
	 * @param nResourceID
	 *            资源ID
	 * @return 取出的字符串
	 */
	public static String toResourceString(int nResourceID) {
		return JKDebug.hContext.getResources().getString(nResourceID);
	}

	/**
	 * 将字符串转换为字节数组(默认UTF-8)
	 * 
	 * @param tText
	 *            字符串
	 * @return 转换后的字节数组
	 */
	public static byte[] toByteArray(String tText) {
		try {
			return tText.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			JKLog.ErrorLog("无法将字符串转成字节数组.原因为" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将输入流转换为字节数组
	 * 
	 * @param is
	 *            字符串
	 * @return 转换后的字节数组
	 */
	public static byte[] toByteArray(InputStream is) {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[1024 * 16];
		int rc;
		try {
			while ((rc = is.read(buff, 0, 1024 * 16)) > 0) {
				swapStream.write(buff, 0, rc);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        return swapStream.toByteArray();
	}

	/**
	 * 将字符串转换为字节数组
	 * 
	 * @param tText
	 *            字符串
	 * @param tEncoding
	 *            字节编码(UTF-8,ISO-8859-1,GBK)
	 * @return 转换后的字节数组
	 */
	public static byte[] toByteArray(String tText, String tEncoding) {
		try {
			return tText.getBytes(tEncoding);
		} catch (UnsupportedEncodingException e) {
			JKLog.ErrorLog("无法将字符串转成字节数组.原因为" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将图片转为字节数组
	 * @param bmp 图片对象
	 * @param nQuality 图片品质(jpg为0~100,小于0为png)
	 * @return 返回图片字节数组
	 */
	public static byte[] toByteArray(Bitmap bmp, int nQuality) {
		if (bmp == null)
			return null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
        if (nQuality < 0)
            bmp.compress(CompressFormat.PNG, 100, output);
        else
            bmp.compress(CompressFormat.JPEG, nQuality, output);
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			JKLog.ErrorLog("图片转为字节数组.原因为" + e.getMessage());
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 将图片转为字节数组
	 * @param bmp 图片对象
	 * @return 返回图片字节数组
	 */
	public static byte[] toByteArray(Bitmap bmp) {
		if (bmp == null)
			return null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		boolean bResult = bmp.compress(CompressFormat.PNG, 100, output);
		if (!bResult)
            bmp.compress(CompressFormat.JPEG, 80, output);
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			JKLog.ErrorLog("图片转为字节数组.原因为" + e.getMessage());
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * 将InputStream转换成String
	 * @param is 输入流
	 * @param tEncoding 字节编码(UTF-8,ISO-8859-1,GBK)
	 * @return 转换后的字符串
	 */
	public static String toString(InputStream is,String tEncoding) {
		StringBuilder sb = new StringBuilder();
		String readline;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is,tEncoding));
			while (br.ready()) {
				readline = br.readLine();
				sb.append(readline);
			}
			br.close();
		} catch (IOException ie) {
			JKLog.ErrorLog("无法将InputStream转成字符串.原因为" + ie.getMessage());
			ie.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 将InputStream转换成String
	 * @param is 输入流
	 * @return 转换后的字符串
	 */
	public static String toString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		String readline;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while (br.ready()) {
				readline = br.readLine();
				sb.append(readline);
			}
			br.close();
		} catch (IOException ie) {
			JKLog.ErrorLog("无法将InputStream转成字符串.原因为" + ie.getMessage());
			ie.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * 将LinkedHashSet模式的字符串数组转成普通数组
	 * @param a_tString 字符串数组
	 * @return 转换后的普通数组
	 */
	public static ArrayList<String> toArrayString(LinkedHashSet<String> a_tString)
	{
		ArrayList<String> a_tArrayList = new ArrayList<>();
        for (String anA_tString : a_tString) {
            a_tArrayList.add(anA_tString);
        }
		return a_tArrayList;
	}
	
	/**
	 * 将普通数组转成LinkedHashSet模式的字符串数组
	 * @param a_tString 普通数组
	 * @return LinkedHashSet模式的字符串数组
	 */
	public static LinkedHashSet<String> toLinkedHashSetString(ArrayList<String> a_tString)
	{
		LinkedHashSet<String> a_tArrayList = new LinkedHashSet<>();
		for (int i=0; i<a_tString.size(); ++i)
		{
			a_tArrayList.add(a_tString.get(i));
		}
		return a_tArrayList;
	}
	
	/**
	 * 简体转繁体
	 * @param tText 简体字符串
	 * @return 转换后的繁体字符串
	 */
	public static String CnToTw(String tText) {
		if (tText == null)
			return null;
		StringBuffer tNewStr = new StringBuffer();
		for (int i = 0; i < tText.length(); i++) {
			char cChar = tText.charAt(i);
			int nIndex = tCn.indexOf(cChar);
			if (nIndex != -1) {
				tNewStr.append(tTw.charAt(nIndex));
			} else {
				tNewStr.append(cChar);
			}
		}
		/* 繁转简词库 */
		for (int i = 0; i < a_tCnBefore.length; ++i) {
			int nIndex = tNewStr.indexOf(a_tCnBefore[i]);
			if (nIndex >= 0) {
				tNewStr = tNewStr.replace(nIndex, nIndex + a_tCnBefore[i].length(), a_tTwAfter[i]);

			}
		}

		return tNewStr.toString();
	}

	/**
	 * 繁转换成简体
	 * 
	 * @param tText
	 *            繁体字符串
	 * @return 转换后的简体字符串
	 */
	public static String TwToCn(String tText) {
        if (tText == null)
			return null;
		StringBuffer tNewStr = new StringBuffer();
		for (int i = 0; i < tText.length(); i++) {
			char cChar = tText.charAt(i);
			int nIndex = tTw.indexOf(cChar);
			if (nIndex != -1) {
				tNewStr.append(tCn.charAt(nIndex));
			} else {
				tNewStr.append(cChar);
			}
		}
		/* 简转繁词库 */
		for (int i = 0; i < a_tTwBefore.length; ++i) {
			int nIndex = tNewStr.indexOf(a_tTwBefore[i]);
			if (nIndex >= 0) {
				tNewStr = tNewStr.replace(nIndex, nIndex + a_tTwBefore[i].length(), a_tCnAfter[i]);
			}
		}

		return tNewStr.toString();
	}

	/**
	 * 简体转换成繁体
	 * 
	 * @param cText
	 *            简体字符
	 * @return 转换后的繁体字符
	 */
	public static char TwToCn(char cText) {
		int nIndex = tTw.indexOf(cText);
		if (nIndex != -1)
			return tCn.charAt(nIndex);
		else {
			return cText;
		}
	}

	/**
	 * 繁体转简体
	 * 
	 * @param cText
	 *            繁体字符
	 * @return 转换后的简体字符
	 */
	public static char CnToTw(char cText) {
		int nIndex = tCn.indexOf(cText);
		if (nIndex != -1) {
			return tTw.charAt(nIndex);
		} else {
			return cText;
		}
	}

	/**
	 * 将字符串转为Url编码
	 * 
	 * @param tString
	 *            需要转换的字符串
	 * @return url编码
	 */
	public static String toURL(String tString) {
		String tBack = "";
		try {
			tBack = java.net.URLEncoder.encode(tString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			JKLog.ErrorLog("无法将字符串转成Url编码.原因为" + e.getMessage());
			e.printStackTrace();
		}
		return tBack;
	}

	/**
	 * 将字符串转为Url编码
	 * 
	 * @param tString
	 *            需要转换的字符串
	 * @param tEncoding
	 *            字符编码(UTF-8,GBK,ISO-8859-1)
	 * @return url编码
	 */
	public static String toURL(String tString, String tEncoding) {
		String tBack = "";
		try {
			tBack = java.net.URLEncoder.encode(tString, tEncoding);
		} catch (UnsupportedEncodingException e) {
			JKLog.ErrorLog("无法将字符串转成Url编码.原因为" + e.getMessage());
			e.printStackTrace();
		}
		return tBack;
	}

	/**
	 * 将url编码转成string
	 * 
	 * @param tString
	 *            url编码
	 * @return 转换编码后的字符串
	 */
	public static String URLToString(String tString) {
		String tBack = "";
		try {
			tBack = URLDecoder.decode(tString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			JKLog.ErrorLog("无法将Url编码转成字符串.原因为" + e.getMessage());
			e.printStackTrace();
		}
		return tBack;
	}

	/**
	 * 将url编码转成string
	 * 
	 * @param tString
	 *            url编码
	 * @param tEncoding
	 *            字符编码(UTF-8,GBK,ISO-8859-1)
	 * @return 转换编码后的字符串
	 */
	public static String URLToString(String tString, String tEncoding) {
		String tBack = "";
		try {
			tBack = URLDecoder.decode(tString, tEncoding);
		} catch (UnsupportedEncodingException e) {
			JKLog.ErrorLog("无法将Url编码转成字符串.原因为" + e.getMessage());
			e.printStackTrace();
		}
		return tBack;
	}

	/**
	 * 将浮点数保留小数后转为字符串
	 * 
	 * @param fNum
	 *            浮点数
	 * @param nDigit
	 *            保留小数位数
	 * @return 转换后的字符串
	 */
	public static String toString(float fNum, int nDigit) {
		StringBuilder tBuffer = new StringBuilder("#.");
		for (int i = 0; i < nDigit; ++i) {
			tBuffer.append("0");
		}
		DecimalFormat df = new DecimalFormat(tBuffer.toString());
		String tBack = (df.format(fNum));
		float fReal = JKConvert.toFloat(tBack);
		if (fReal < 1 && fReal >= 0)
			return "0" + tBack;
		else if (fReal < 0 && fReal > -1)
			return "-0" + tBack.substring(1);
		else
			return tBack;
	}

	/**
	 * 将浮点数保留小数后转为字符串
	 * 
	 * @param dNum
	 *            浮点数
	 * @param nDigit
	 *            保留小数位数
	 * @return 转换后的字符串
	 */
	public static String toString(double dNum, int nDigit) {
		StringBuilder tBuffer = new StringBuilder("#.");
		for (int i = 0; i < nDigit; ++i) {
			tBuffer.append("0");
		}
		DecimalFormat df = new DecimalFormat(tBuffer.toString());
		String tBack = (df.format(dNum));
		float fReal = JKConvert.toFloat(tBack);
		if (fReal < 1 && fReal >= 0)
			return "0" + tBack;
		else if (fReal < 0 && fReal > -1)
			return "-0" + tBack.substring(1);
		else
			return tBack;
	}
	
	/**
	 * Dip转像素
	 * 
	 * @param fDip
	 *            dip值
	 * @return 像素值
	 */
	public static int DipToPx(float fDip) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, fDip, JKDebug.hContext
				.getResources().getDisplayMetrics());
	}

	/**
	 * sp转像素
	 * 
	 * @param fSP
	 *            sp值
	 * @return 像素值
	 */
	public static int SpToPx(float fSP) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, fSP, JKDebug.hContext
				.getResources().getDisplayMetrics());
	}

	/**
	 * 汉语拼音转换
	 * 
	 * @param tChinese
	 *            需要转换的中文
	 * @return 转换后的汉语拼音缩写(小写)
	 */
	public static String toPinYin(String tChinese) {
		if (tChinese == null || tChinese.length() == 0)
			return "";
		StringBuilder tPinyinString = new StringBuilder();
		char[] charArray = tChinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		try {
			// 遍历数组，ASC码大于128进行转换
            for (char aCharArray : charArray) {
                if (aCharArray > 128) {
                    // charAt(0)取出首字母
                    if (aCharArray >= 0x4e00 && aCharArray <= 0x9fa5) { // 判断是否中文
						String[] a_tResult = PinyinHelper.toHanyuPinyinStringArray(aCharArray,defaultFormat);
						if (a_tResult == null)
							return tChinese;
						if (a_tResult.length == 0)
							tPinyinString.append(aCharArray);
						else {
							tPinyinString.append(a_tResult[0].charAt(0));
						}
                    } else { // 不是中文的打上未知，所以无法处理韩文日本等等其他文字
                        tPinyinString.append(aCharArray);
                    }
                } else {
                    tPinyinString.append(aCharArray);
                }
            }
			return tPinyinString.toString();
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
			JKLog.ErrorLog("无法将字符串转换为拼音.原因为" + e.getMessage());
			return "";
		}
	}

	/**
	 * 汉语拼音转换
	 *
	 * @param tChinese
	 *            需要转换的中文
	 * @return 转换后的汉语拼音缩写(小写)
	 */
	public static String toFullPinYin(String tChinese) {
		if (tChinese == null || tChinese.length() == 0)
			return "";
		StringBuilder tPinyinString = new StringBuilder();
		char[] charArray = tChinese.toCharArray();
        // 遍历数组，ASC码大于128进行转换
        for (char aCharArray : charArray) {
            if (aCharArray > 128) {
                // charAt(0)取出首字母
                if (aCharArray >= 0x4e00 && aCharArray <= 0x9fa5) { // 判断是否中文
					String[] a_tResult = PinyinHelper.toHanyuPinyinStringArray(aCharArray);
					if (a_tResult == null)
						return tChinese;
					if (a_tResult.length == 0)
						tPinyinString.append(aCharArray);
					else {
						String tPinYin = a_tResult[0];
						tPinyinString.append(tPinYin.substring(0, tPinYin.length()-1));
					}
                } else { // 不是中文的打上未知，所以无法处理韩文日本等等其他文字
                    tPinyinString.append(aCharArray);
                }
            } else {
                tPinyinString.append(aCharArray);
            }
        }
        return tPinyinString.toString();
    }

	/**
	 * 小数四舍五入
	 * 
	 * @param dNum
	 *            原小数
	 * @param nIndex
	 *            保留的位数
	 * @return 返回四舍五入的结果
	 */
	public static String toFormatDecimal(double dNum, int nIndex) {
		return new BigDecimal(dNum).setScale(nIndex, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**
	 * 拼接符合文本
	 * @param a_tString 需要拼接的符合文本分段字符串
	 * @param a_csStyle 对应每一段的文本样式
	 * @return 拼接后的符合文本
	 */
	public static SpannableStringBuilder MakeSpannableString(ArrayList<String> a_tString,ArrayList<CharacterStyle> a_csStyle)
	{
		SpannableStringBuilder ssbString = new SpannableStringBuilder();
		for (int i = 0; i < a_tString.size(); i++) {
			SpannableString ssTmp = new SpannableString(a_tString.get(i));
			ssTmp.setSpan(a_csStyle.get(i), 0, ssTmp.length(),Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			ssbString.append(ssTmp);
		}
		return ssbString;
	}

	/**
	 * 根据uri获取绝对路径
	 * @param uri uri对象
	 * @return sd卡上路径
	 */
	public static String UriToPath(Uri uri)
	{
		return getPath(JKDebug.hContext,uri);
	}

	/**
	 * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
	 */
	@SuppressLint("NewApi")
	private static String getPath(final Context context, final Uri uri) {
		if (uri == null)
			return null;
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	private static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	private static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	private static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	private static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}
}