package com.qiuxs.cuteframework.core.basic.utils.ip2region.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.qiuxs.cuteframework.core.basic.bean.Pair;
import com.qiuxs.cuteframework.core.basic.utils.CollectionUtils;
import com.qiuxs.cuteframework.core.basic.utils.generator.RandomGenerator;
import com.qiuxs.cuteframework.core.basic.utils.ip2region.Ip2RegionFacade;

/**
 * 区域常量
 * @author qiuxs
 * 2019年6月15日 下午9:41:44
 */
public class RegionConstants {

	/**[规范省名,规范省编码]*/
	private static BiMap<String, Integer> provCodeBiMap = HashBiMap.create();
	private static Map<String, Integer> provCodeMap = new HashMap<String, Integer>();
	private static List<Integer> provCodeList = new ArrayList<Integer>();

	private static BiMap<String, Integer> cityCodeBiMap = HashBiMap.create();
	private static Map<String, Integer> cityCodeMap = new HashMap<String, Integer>();
	private static Map<Integer, List<Integer>> provCodeCityCodesMap = new HashMap<>();
	private static Map<Integer, BiMap<String, Integer>> provCodeCityNameCityCodeBiMapMap = new HashMap<>();

	static {

		/**********************************省******************************/
		provCodeBiMap.put("北京市", 110000);
		provCodeBiMap.put("天津市", 120000);
		provCodeBiMap.put("河北省", 130000);
		provCodeBiMap.put("山西省", 140000);
		provCodeBiMap.put("内蒙古自治区", 150000);

		provCodeBiMap.put("辽宁省", 210000);
		provCodeBiMap.put("吉林省", 220000);
		provCodeBiMap.put("黑龙江省", 230000);

		provCodeBiMap.put("上海市", 310000);
		provCodeBiMap.put("江苏省", 320000);
		provCodeBiMap.put("浙江省", 330000);
		provCodeBiMap.put("安徽省", 340000);
		provCodeBiMap.put("福建省", 350000);
		provCodeBiMap.put("江西省", 360000);
		provCodeBiMap.put("山东省", 370000);

		provCodeBiMap.put("河南省", 410000);
		provCodeBiMap.put("湖北省", 420000);
		provCodeBiMap.put("湖南省", 430000);
		provCodeBiMap.put("广东省", 440000);
		provCodeBiMap.put("广西壮族自治区", 450000);
		provCodeBiMap.put("海南省", 460000);

		provCodeBiMap.put("重庆市", 500000);
		provCodeBiMap.put("四川省", 510000);
		provCodeBiMap.put("贵州省", 520000);
		provCodeBiMap.put("云南省", 530000);
		provCodeBiMap.put("西藏自治区", 540000);

		provCodeBiMap.put("陕西省", 610000);
		provCodeBiMap.put("甘肃省", 620000);
		provCodeBiMap.put("青海省", 630000);
		provCodeBiMap.put("宁夏回族自治区", 640000);
		provCodeBiMap.put("新疆维吾尔自治区", 650000);

		provCodeBiMap.put("台湾省", 710000);
		provCodeBiMap.put("香港特别行政区", 810000);
		provCodeBiMap.put("澳门特别行政区", 820000);

		provCodeMap.putAll(provCodeBiMap);
		provCodeList.addAll(provCodeMap.values());
		/***************************省别名********************************/
		provCodeMap.put("北京", 110000);
		provCodeMap.put("天津", 120000);
		provCodeMap.put("河北", 130000);
		provCodeMap.put("山西", 140000);
		provCodeMap.put("内蒙古", 150000);

		provCodeMap.put("辽宁", 210000);
		provCodeMap.put("吉林", 220000);
		provCodeMap.put("黑龙江", 230000);

		provCodeMap.put("上海", 310000);
		provCodeMap.put("江苏", 320000);
		provCodeMap.put("浙江", 330000);
		provCodeMap.put("安徽", 340000);
		provCodeMap.put("福建", 350000);
		provCodeMap.put("江西", 360000);
		provCodeMap.put("山东", 370000);

		provCodeMap.put("河南", 410000);
		provCodeMap.put("湖北", 420000);
		provCodeMap.put("湖南", 430000);
		provCodeMap.put("广东", 440000);
		provCodeMap.put("广西", 450000);
		provCodeMap.put("海南", 460000);

		provCodeMap.put("重庆", 500000);
		provCodeMap.put("四川", 510000);
		provCodeMap.put("贵州", 520000);
		provCodeMap.put("云南", 530000);
		provCodeMap.put("西藏", 540000);

		provCodeMap.put("陕西", 610000);
		provCodeMap.put("甘肃", 620000);
		provCodeMap.put("青海", 630000);
		provCodeMap.put("宁夏", 640000);
		provCodeMap.put("新疆", 650000);

		provCodeMap.put("台湾", 710000);
		provCodeMap.put("香港", 810000);
		provCodeMap.put("澳门", 820000);

		/*********************************市*********************************/
		{
			BiMap<String, Integer> bjBiMap = HashBiMap.create();
			bjBiMap.put("北京市", 110100);
			//		cityCodeMap.put("县" , 110200);
			provCodeCityNameCityCodeBiMapMap.put(110000, bjBiMap);
		}
		{
			BiMap<String, Integer> tjBiMap = HashBiMap.create();
			tjBiMap.put("天津市", 120100);
			//		cityCodeMap.put("市辖县" , 120200);
			provCodeCityNameCityCodeBiMapMap.put(120000, tjBiMap);
		}
		{
			BiMap<String, Integer> hbBiMap = HashBiMap.create();
			hbBiMap.put("石家庄市", 130100);
			hbBiMap.put("唐山市", 130200);
			hbBiMap.put("秦皇岛市", 130300);
			hbBiMap.put("邯郸市", 130400);
			hbBiMap.put("邢台市", 130500);
			hbBiMap.put("保定市", 130600);
			hbBiMap.put("张家口市", 130700);
			hbBiMap.put("承德市", 130800);
			hbBiMap.put("沧州市", 130900);
			hbBiMap.put("廊坊市", 131000);
			hbBiMap.put("衡水市", 131100);
			provCodeCityNameCityCodeBiMapMap.put(130000, hbBiMap);
		}
		{
			BiMap<String, Integer> sxBiMap = HashBiMap.create();
			sxBiMap.put("太原市", 140100);
			sxBiMap.put("大同市", 140200);
			sxBiMap.put("阳泉市", 140300);
			sxBiMap.put("长治市", 140400);
			sxBiMap.put("晋城市", 140500);
			sxBiMap.put("朔州市", 140600);
			sxBiMap.put("晋中市", 140700);
			sxBiMap.put("运城市", 140800);
			sxBiMap.put("忻州市", 140900);
			sxBiMap.put("临汾市", 141000);
			sxBiMap.put("吕梁市", 141100);
			provCodeCityNameCityCodeBiMapMap.put(140000, sxBiMap);
		}
		{
			BiMap<String, Integer> nmgBiMap = HashBiMap.create();
			nmgBiMap.put("呼和浩特市", 150100);
			nmgBiMap.put("包头市", 150200);
			nmgBiMap.put("乌海市", 150300);
			nmgBiMap.put("赤峰市", 150400);
			nmgBiMap.put("通辽市", 150500);
			nmgBiMap.put("鄂尔多斯市", 150600);
			nmgBiMap.put("呼伦贝尔市", 150700);
			nmgBiMap.put("巴彦淖尔市", 150800);
			nmgBiMap.put("乌兰察布市", 150900);
			nmgBiMap.put("兴安盟", 152200);
			nmgBiMap.put("锡林郭勒盟", 152500);
			nmgBiMap.put("阿拉善盟", 152900);
			provCodeCityNameCityCodeBiMapMap.put(150000, nmgBiMap);
		}
		{
			BiMap<String, Integer> lnBiMap = HashBiMap.create();
			lnBiMap.put("沈阳市", 210100);
			lnBiMap.put("大连市", 210200);
			lnBiMap.put("鞍山市", 210300);
			lnBiMap.put("抚顺市", 210400);
			lnBiMap.put("本溪市", 210500);
			lnBiMap.put("丹东市", 210600);
			lnBiMap.put("锦州市", 210700);
			lnBiMap.put("营口市", 210800);
			lnBiMap.put("阜新市", 210900);
			lnBiMap.put("辽阳市", 211000);
			lnBiMap.put("盘锦市", 211100);
			lnBiMap.put("铁岭市", 211200);
			lnBiMap.put("朝阳市", 211300);
			lnBiMap.put("葫芦岛市", 211400);
			provCodeCityNameCityCodeBiMapMap.put(210000, lnBiMap);
		}
		{
			BiMap<String, Integer> jlBiMap = HashBiMap.create();
			jlBiMap.put("长春市", 220100);
			jlBiMap.put("吉林市", 220200);
			jlBiMap.put("四平市", 220300);
			jlBiMap.put("辽源市", 220400);
			jlBiMap.put("通化市", 220500);
			jlBiMap.put("白山市", 220600);
			jlBiMap.put("松原市", 220700);
			jlBiMap.put("白城市", 220800);
			jlBiMap.put("延边朝鲜族自治州", 222400);
			provCodeCityNameCityCodeBiMapMap.put(220000, jlBiMap);
		}
		{
			BiMap<String, Integer> hljBiMap = HashBiMap.create();
			hljBiMap.put("哈尔滨市", 230100);
			hljBiMap.put("齐齐哈尔市", 230200);
			hljBiMap.put("鸡西市", 230300);
			hljBiMap.put("鹤岗市", 230400);
			hljBiMap.put("双鸭山市", 230500);
			hljBiMap.put("大庆市", 230600);
			hljBiMap.put("伊春市", 230700);
			hljBiMap.put("佳木斯市", 230800);
			hljBiMap.put("七台河市", 230900);
			hljBiMap.put("牡丹江市", 231000);
			hljBiMap.put("黑河市", 231100);
			hljBiMap.put("绥化市", 231200);
			hljBiMap.put("大兴安岭地区", 232700);
			provCodeCityNameCityCodeBiMapMap.put(230000, hljBiMap);
		}
		{
			BiMap<String, Integer> shBiMap = HashBiMap.create();
			shBiMap.put("上海市", 310100);
			//		cityCodeMap.put("县" , 310200);
			provCodeCityNameCityCodeBiMapMap.put(310000, shBiMap);
		}
		{
			BiMap<String, Integer> jsBiMap = HashBiMap.create();
			jsBiMap.put("南京市", 320100);
			jsBiMap.put("无锡市", 320200);
			jsBiMap.put("徐州市", 320300);
			jsBiMap.put("常州市", 320400);
			jsBiMap.put("苏州市", 320500);
			jsBiMap.put("南通市", 320600);
			jsBiMap.put("连云港市", 320700);
			jsBiMap.put("淮安市", 320800);
			jsBiMap.put("盐城市", 320900);
			jsBiMap.put("扬州市", 321000);
			jsBiMap.put("镇江市", 321100);
			jsBiMap.put("泰州市", 321200);
			jsBiMap.put("宿迁市", 321300);
			provCodeCityNameCityCodeBiMapMap.put(320000, jsBiMap);
		}
		{
			BiMap<String, Integer> zjBiMap = HashBiMap.create();
			zjBiMap.put("杭州市", 330100);
			zjBiMap.put("宁波市", 330200);
			zjBiMap.put("温州市", 330300);
			zjBiMap.put("嘉兴市", 330400);
			zjBiMap.put("湖州市", 330500);
			zjBiMap.put("绍兴市", 330600);
			zjBiMap.put("金华市", 330700);
			zjBiMap.put("衢州市", 330800);
			zjBiMap.put("舟山市", 330900);
			zjBiMap.put("台州市", 331000);
			zjBiMap.put("丽水市", 331100);
			provCodeCityNameCityCodeBiMapMap.put(330000, zjBiMap);
		}
		{
			BiMap<String, Integer> ahBiMap = HashBiMap.create();
			ahBiMap.put("合肥市", 340100);
			ahBiMap.put("芜湖市", 340200);
			ahBiMap.put("蚌埠市", 340300);
			ahBiMap.put("淮南市", 340400);
			ahBiMap.put("马鞍山市", 340500);
			ahBiMap.put("淮北市", 340600);
			ahBiMap.put("铜陵市", 340700);
			ahBiMap.put("安庆市", 340800);
			ahBiMap.put("黄山市", 341000);
			ahBiMap.put("滁州市", 341100);
			ahBiMap.put("阜阳市", 341200);
			ahBiMap.put("宿州市", 341300);
			ahBiMap.put("巢湖市", 341400);
			ahBiMap.put("六安市", 341500);
			ahBiMap.put("亳州市", 341600);
			ahBiMap.put("池州市", 341700);
			ahBiMap.put("宣城市", 341800);
			provCodeCityNameCityCodeBiMapMap.put(340000, ahBiMap);
		}
		{
			BiMap<String, Integer> fjBiMap = HashBiMap.create();
			fjBiMap.put("福州市", 350100);
			fjBiMap.put("厦门市", 350200);
			fjBiMap.put("莆田市", 350300);
			fjBiMap.put("三明市", 350400);
			fjBiMap.put("泉州市", 350500);
			fjBiMap.put("漳州市", 350600);
			fjBiMap.put("南平市", 350700);
			fjBiMap.put("龙岩市", 350800);
			fjBiMap.put("宁德市", 350900);
			provCodeCityNameCityCodeBiMapMap.put(350000, fjBiMap);
		}
		{
			BiMap<String, Integer> jxBiMap = HashBiMap.create();
			jxBiMap.put("南昌市", 360100);
			jxBiMap.put("景德镇市", 360200);
			jxBiMap.put("萍乡市", 360300);
			jxBiMap.put("九江市", 360400);
			jxBiMap.put("新余市", 360500);
			jxBiMap.put("鹰潭市", 360600);
			jxBiMap.put("赣州市", 360700);
			jxBiMap.put("吉安市", 360800);
			jxBiMap.put("宜春市", 360900);
			jxBiMap.put("抚州市", 361000);
			jxBiMap.put("上饶市", 361100);
			provCodeCityNameCityCodeBiMapMap.put(360000, jxBiMap);
		}
		{
			BiMap<String, Integer> sdBiMap = HashBiMap.create();
			sdBiMap.put("济南市", 370100);
			sdBiMap.put("青岛市", 370200);
			sdBiMap.put("淄博市", 370300);
			sdBiMap.put("枣庄市", 370400);
			sdBiMap.put("东营市", 370500);
			sdBiMap.put("烟台市", 370600);
			sdBiMap.put("潍坊市", 370700);
			sdBiMap.put("济宁市", 370800);
			sdBiMap.put("泰安市", 370900);
			sdBiMap.put("威海市", 371000);
			sdBiMap.put("日照市", 371100);
			sdBiMap.put("莱芜市", 371200);
			sdBiMap.put("临沂市", 371300);
			sdBiMap.put("德州市", 371400);
			sdBiMap.put("聊城市", 371500);
			sdBiMap.put("滨州市", 371600);
			sdBiMap.put("菏泽市", 371700);
			provCodeCityNameCityCodeBiMapMap.put(370000, sdBiMap);
		}
		{
			BiMap<String, Integer> hnBiMap = HashBiMap.create();
			hnBiMap.put("郑州市", 410100);
			hnBiMap.put("开封市", 410200);
			hnBiMap.put("洛阳市", 410300);
			hnBiMap.put("平顶山市", 410400);
			hnBiMap.put("安阳市", 410500);
			hnBiMap.put("鹤壁市", 410600);
			hnBiMap.put("新乡市", 410700);
			hnBiMap.put("焦作市", 410800);
			hnBiMap.put("濮阳市", 410900);
			hnBiMap.put("许昌市", 411000);
			hnBiMap.put("漯河市", 411100);
			hnBiMap.put("三门峡市", 411200);
			hnBiMap.put("南阳市", 411300);
			hnBiMap.put("商丘市", 411400);
			hnBiMap.put("信阳市", 411500);
			hnBiMap.put("周口市", 411600);
			hnBiMap.put("驻马店市", 411700);
			provCodeCityNameCityCodeBiMapMap.put(410000, hnBiMap);
		}
		{
			BiMap<String, Integer> hbBiMap = HashBiMap.create();
			hbBiMap.put("武汉市", 420100);
			hbBiMap.put("黄石市", 420200);
			hbBiMap.put("十堰市", 420300);
			hbBiMap.put("宜昌市", 420500);
			hbBiMap.put("襄樊市", 420600);
			hbBiMap.put("鄂州市", 420700);
			hbBiMap.put("荆门市", 420800);
			hbBiMap.put("孝感市", 420900);
			hbBiMap.put("荆州市", 421000);
			hbBiMap.put("黄冈市", 421100);
			hbBiMap.put("咸宁市", 421200);
			hbBiMap.put("随州市", 421300);
			hbBiMap.put("恩施州", 422800);
			//		cityCodeMap.put("省直辖行政单位" , 429000);
			provCodeCityNameCityCodeBiMapMap.put(420000, hbBiMap);
		}
		{
			BiMap<String, Integer> hnBiMap = HashBiMap.create();
			hnBiMap.put("长沙市", 430100);
			hnBiMap.put("株洲市", 430200);
			hnBiMap.put("湘潭市", 430300);
			hnBiMap.put("衡阳市", 430400);
			hnBiMap.put("邵阳市", 430500);
			hnBiMap.put("岳阳市", 430600);
			hnBiMap.put("常德市", 430700);
			hnBiMap.put("张家界市", 430800);
			hnBiMap.put("益阳市", 430900);
			hnBiMap.put("郴州市", 431000);
			hnBiMap.put("永州市", 431100);
			hnBiMap.put("怀化市", 431200);
			hnBiMap.put("娄底市", 431300);
			hnBiMap.put("湘西土家族苗族自治州", 433100);
			provCodeCityNameCityCodeBiMapMap.put(430000, hnBiMap);
		}
		{
			BiMap<String, Integer> gdBiMap = HashBiMap.create();
			gdBiMap.put("广州市", 440100);
			gdBiMap.put("韶关市", 440200);
			gdBiMap.put("深圳市", 440300);
			gdBiMap.put("珠海市", 440400);
			gdBiMap.put("汕头市", 440500);
			gdBiMap.put("佛山市", 440600);
			gdBiMap.put("江门市", 440700);
			gdBiMap.put("湛江市", 440800);
			gdBiMap.put("茂名市", 440900);
			gdBiMap.put("肇庆市", 441200);
			gdBiMap.put("惠州市", 441300);
			gdBiMap.put("梅州市", 441400);
			gdBiMap.put("汕尾市", 441500);
			gdBiMap.put("河源市", 441600);
			gdBiMap.put("阳江市", 441700);
			gdBiMap.put("清远市", 441800);
			gdBiMap.put("东莞市", 441900);
			gdBiMap.put("中山市", 442000);
			gdBiMap.put("石岐区", 442100);
			gdBiMap.put("东区", 442200);
			gdBiMap.put("火炬高技术产业开发区", 442300);
			gdBiMap.put("西区", 442400);
			gdBiMap.put("南区", 442500);
			gdBiMap.put("五桂山", 442600);
			gdBiMap.put("潮州市", 445100);
			gdBiMap.put("揭阳市", 445200);
			gdBiMap.put("云浮市", 445300);
			provCodeCityNameCityCodeBiMapMap.put(440000, gdBiMap);
		}
		{
			BiMap<String, Integer> gxBiMap = HashBiMap.create();
			gxBiMap.put("南宁市", 450100);
			gxBiMap.put("柳州市", 450200);
			gxBiMap.put("桂林市", 450300);
			gxBiMap.put("梧州市", 450400);
			gxBiMap.put("北海市", 450500);
			gxBiMap.put("防城港市", 450600);
			gxBiMap.put("钦州市", 450700);
			gxBiMap.put("贵港市", 450800);
			gxBiMap.put("玉林市", 450900);
			gxBiMap.put("百色市", 451000);
			gxBiMap.put("贺州市", 451100);
			gxBiMap.put("河池市", 451200);
			gxBiMap.put("来宾市", 451300);
			gxBiMap.put("崇左市", 451400);
			provCodeCityNameCityCodeBiMapMap.put(450000, gxBiMap);
		}
		{
			BiMap<String, Integer> hnBiMap = HashBiMap.create();
			hnBiMap.put("海口市", 460100);
			hnBiMap.put("三亚市", 460200);
			//		cityCodeMap.put("省直辖县级行政区划" , 469000);
			provCodeCityNameCityCodeBiMapMap.put(460000, hnBiMap);

		}
		{
			BiMap<String, Integer> cqBiMap = HashBiMap.create();
			cqBiMap.put("重庆市", 500100);
			//		cityCodeMap.put("县" , 500200);
			provCodeCityNameCityCodeBiMapMap.put(500000, cqBiMap);
		}
		{
			BiMap<String, Integer> scBiMap = HashBiMap.create();
			scBiMap.put("成都市", 510100);
			scBiMap.put("自贡市", 510300);
			scBiMap.put("攀枝花市", 510400);
			scBiMap.put("泸州市", 510500);
			scBiMap.put("德阳市", 510600);
			scBiMap.put("绵阳市", 510700);
			scBiMap.put("广元市", 510800);
			scBiMap.put("遂宁市", 510900);
			scBiMap.put("内江市", 511000);
			scBiMap.put("乐山市", 511100);
			scBiMap.put("南充市", 511300);
			scBiMap.put("眉山市", 511400);
			scBiMap.put("宜宾市", 511500);
			scBiMap.put("广安市", 511600);
			scBiMap.put("达州市", 511700);
			scBiMap.put("雅安市", 511800);
			scBiMap.put("巴中市", 511900);
			scBiMap.put("资阳市", 512000);
			scBiMap.put("阿坝州", 513200);
			scBiMap.put("甘孜藏族自治州", 513300);
			scBiMap.put("凉山州", 513400);
			provCodeCityNameCityCodeBiMapMap.put(510000, scBiMap);
		}
		{
			BiMap<String, Integer> gzBiMap = HashBiMap.create();
			gzBiMap.put("贵阳市", 520100);
			gzBiMap.put("六盘水市", 520200);
			gzBiMap.put("遵义市", 520300);
			gzBiMap.put("安顺市", 520400);
			gzBiMap.put("铜仁地区", 522200);
			gzBiMap.put("黔西南州", 522300);
			gzBiMap.put("毕节地区", 522400);
			gzBiMap.put("黔东南苗族侗族自治州", 522600);
			gzBiMap.put("黔南布依族苗族自治州", 522700);
			provCodeCityNameCityCodeBiMapMap.put(520000, gzBiMap);
		}
		{
			BiMap<String, Integer> ynBiMap = HashBiMap.create();
			ynBiMap.put("昆明市", 530100);
			ynBiMap.put("曲靖市", 530300);
			ynBiMap.put("玉溪市", 530400);
			ynBiMap.put("保山市", 530500);
			ynBiMap.put("昭通市", 530600);
			ynBiMap.put("丽江市", 530700);
			ynBiMap.put("思茅市", 530800);
			ynBiMap.put("临沧市", 530900);
			ynBiMap.put("楚雄州", 532300);
			ynBiMap.put("红河州", 532500);
			ynBiMap.put("文山州", 532600);
			ynBiMap.put("西双版纳州", 532800);
			ynBiMap.put("大理州", 532900);
			ynBiMap.put("德宏州", 533100);
			ynBiMap.put("怒江州", 533300);
			ynBiMap.put("迪庆州", 533400);
			provCodeCityNameCityCodeBiMapMap.put(530000, ynBiMap);
		}
		{
			BiMap<String, Integer> lsBiMap = HashBiMap.create();
			lsBiMap.put("拉萨市", 540100);
			lsBiMap.put("昌都地区", 542100);
			lsBiMap.put("山南地区", 542200);
			lsBiMap.put("日喀则地区", 542300);
			lsBiMap.put("那曲地区", 542400);
			lsBiMap.put("阿里地区", 542500);
			lsBiMap.put("林芝地区", 542600);
			provCodeCityNameCityCodeBiMapMap.put(540000, lsBiMap);
		}
		{
			BiMap<String, Integer> sxBiMap = HashBiMap.create();
			sxBiMap.put("西安市", 610100);
			sxBiMap.put("铜川市", 610200);
			sxBiMap.put("宝鸡市", 610300);
			sxBiMap.put("咸阳市", 610400);
			sxBiMap.put("渭南市", 610500);
			sxBiMap.put("延安市", 610600);
			sxBiMap.put("汉中市", 610700);
			sxBiMap.put("榆林市", 610800);
			sxBiMap.put("安康市", 610900);
			sxBiMap.put("商洛市", 611000);
			provCodeCityNameCityCodeBiMapMap.put(610000, sxBiMap);
		}
		{
			BiMap<String, Integer> gsBiMap = HashBiMap.create();
			gsBiMap.put("兰州市", 620100);
			gsBiMap.put("嘉峪关市", 620200);
			gsBiMap.put("金昌市", 620300);
			gsBiMap.put("白银市", 620400);
			gsBiMap.put("天水市", 620500);
			gsBiMap.put("武威市", 620600);
			gsBiMap.put("张掖市", 620700);
			gsBiMap.put("平凉市", 620800);
			gsBiMap.put("酒泉市", 620900);
			gsBiMap.put("庆阳市", 621000);
			gsBiMap.put("定西市", 621100);
			gsBiMap.put("陇南市", 621200);
			gsBiMap.put("临夏州", 622900);
			gsBiMap.put("甘南州", 623000);
			provCodeCityNameCityCodeBiMapMap.put(620000, gsBiMap);
		}
		{
			BiMap<String, Integer> qhBiMap = HashBiMap.create();
			qhBiMap.put("西宁市", 630100);
			qhBiMap.put("海东地区", 632100);
			qhBiMap.put("海北州", 632200);
			qhBiMap.put("黄南州", 632300);
			qhBiMap.put("海南州", 632500);
			qhBiMap.put("果洛州", 632600);
			qhBiMap.put("玉树州", 632700);
			qhBiMap.put("海西州", 632800);
			provCodeCityNameCityCodeBiMapMap.put(630000, qhBiMap);
		}
		{
			BiMap<String, Integer> nxBiMap = HashBiMap.create();
			nxBiMap.put("银川市", 640100);
			nxBiMap.put("石嘴山市", 640200);
			nxBiMap.put("吴忠市", 640300);
			nxBiMap.put("固原市", 640400);
			nxBiMap.put("中卫市", 640500);
			provCodeCityNameCityCodeBiMapMap.put(640000, nxBiMap);
		}
		{
			BiMap<String, Integer> xjBiMap = HashBiMap.create();
			xjBiMap.put("乌鲁木齐市", 650100);
			xjBiMap.put("克拉玛依市", 650200);
			xjBiMap.put("吐鲁番地区", 652100);
			xjBiMap.put("哈密地区", 652200);
			xjBiMap.put("昌吉州", 652300);
			xjBiMap.put("博尔塔拉蒙古自治州", 652700);
			xjBiMap.put("巴音郭楞蒙古自治州", 652800);
			xjBiMap.put("阿克苏地区", 652900);
			xjBiMap.put("克州", 653000);
			xjBiMap.put("喀什地区", 653100);
			xjBiMap.put("和田地区", 653200);
			xjBiMap.put("伊犁州", 654000);
			xjBiMap.put("塔城地区", 654200);
			xjBiMap.put("阿勒泰地区", 654300);
			//			cityCodeMap.put("省直辖行政单位" , 659000);
			provCodeCityNameCityCodeBiMapMap.put(650000, xjBiMap);
		}

		//归集到cityCodeBiMap
		for (Integer provCode : provCodeCityNameCityCodeBiMapMap.keySet()) {
			BiMap<String, Integer> cityNameCityCodeBiMap = provCodeCityNameCityCodeBiMapMap.get(provCode);
			cityCodeBiMap.putAll(cityNameCityCodeBiMap);
			provCodeCityCodesMap.put(provCode, new ArrayList<>(cityNameCityCodeBiMap.values()));
		}
		cityCodeMap.putAll(cityCodeBiMap);

		/********************************市别名********************************/
		cityCodeMap.put("北京", 110100);
		cityCodeMap.put("天津", 120100);
		cityCodeMap.put("石家庄", 130100);
		cityCodeMap.put("唐山", 130200);
		cityCodeMap.put("秦皇岛", 130300);
		cityCodeMap.put("邯郸", 130400);
		cityCodeMap.put("邢台", 130500);
		cityCodeMap.put("保定", 130600);
		cityCodeMap.put("张家口", 130700);
		cityCodeMap.put("承德", 130800);
		cityCodeMap.put("沧州", 130900);
		cityCodeMap.put("廊坊", 131000);
		cityCodeMap.put("衡水", 131100);
		cityCodeMap.put("太原", 140100);
		cityCodeMap.put("大同", 140200);
		cityCodeMap.put("阳泉", 140300);
		cityCodeMap.put("长治", 140400);
		cityCodeMap.put("晋城", 140500);
		cityCodeMap.put("朔州", 140600);
		cityCodeMap.put("晋中", 140700);
		cityCodeMap.put("运城", 140800);
		cityCodeMap.put("忻州", 140900);
		cityCodeMap.put("临汾", 141000);
		cityCodeMap.put("吕梁", 141100);
		cityCodeMap.put("呼和浩特", 150100);
		cityCodeMap.put("包头", 150200);
		cityCodeMap.put("乌海", 150300);
		cityCodeMap.put("赤峰", 150400);
		cityCodeMap.put("通辽", 150500);
		cityCodeMap.put("鄂尔多斯", 150600);
		cityCodeMap.put("呼伦贝尔", 150700);
		cityCodeMap.put("巴彦淖尔", 150800);
		cityCodeMap.put("乌兰察布", 150900);
		cityCodeMap.put("兴安", 152200);
		cityCodeMap.put("锡林郭勒", 152500);
		cityCodeMap.put("阿拉善", 152900);
		cityCodeMap.put("沈阳", 210100);
		cityCodeMap.put("大连", 210200);
		cityCodeMap.put("鞍山", 210300);
		cityCodeMap.put("抚顺", 210400);
		cityCodeMap.put("本溪", 210500);
		cityCodeMap.put("丹东", 210600);
		cityCodeMap.put("锦州", 210700);
		cityCodeMap.put("营口", 210800);
		cityCodeMap.put("阜新", 210900);
		cityCodeMap.put("辽阳", 211000);
		cityCodeMap.put("盘锦", 211100);
		cityCodeMap.put("铁岭", 211200);
		cityCodeMap.put("朝阳", 211300);
		cityCodeMap.put("葫芦岛", 211400);
		cityCodeMap.put("长春", 220100);
		cityCodeMap.put("吉林", 220200);
		cityCodeMap.put("四平", 220300);
		cityCodeMap.put("辽源", 220400);
		cityCodeMap.put("通化", 220500);
		cityCodeMap.put("白山", 220600);
		cityCodeMap.put("松原", 220700);
		cityCodeMap.put("白城", 220800);
		cityCodeMap.put("延边", 222400);
		cityCodeMap.put("哈尔滨", 230100);
		cityCodeMap.put("齐齐哈尔", 230200);
		cityCodeMap.put("鸡西", 230300);
		cityCodeMap.put("鹤岗", 230400);
		cityCodeMap.put("双鸭山", 230500);
		cityCodeMap.put("大庆", 230600);
		cityCodeMap.put("伊春", 230700);
		cityCodeMap.put("佳木斯", 230800);
		cityCodeMap.put("七台河", 230900);
		cityCodeMap.put("牡丹江", 231000);
		cityCodeMap.put("黑河", 231100);
		cityCodeMap.put("绥化", 231200);
		cityCodeMap.put("大兴安岭", 232700);
		cityCodeMap.put("上海", 310100);
		//		cityCodeMap.put("县" , 310200);
		cityCodeMap.put("南京", 320100);
		cityCodeMap.put("无锡", 320200);
		cityCodeMap.put("徐州", 320300);
		cityCodeMap.put("常州", 320400);
		cityCodeMap.put("苏州", 320500);
		cityCodeMap.put("南通", 320600);
		cityCodeMap.put("连云港", 320700);
		cityCodeMap.put("淮安", 320800);
		cityCodeMap.put("盐城", 320900);
		cityCodeMap.put("扬州", 321000);
		cityCodeMap.put("镇江", 321100);
		cityCodeMap.put("泰州", 321200);
		cityCodeMap.put("宿迁", 321300);
		cityCodeMap.put("杭州", 330100);
		cityCodeMap.put("宁波", 330200);
		cityCodeMap.put("温州", 330300);
		cityCodeMap.put("嘉兴", 330400);
		cityCodeMap.put("湖州", 330500);
		cityCodeMap.put("绍兴", 330600);
		cityCodeMap.put("金华", 330700);
		cityCodeMap.put("衢州", 330800);
		cityCodeMap.put("舟山", 330900);
		cityCodeMap.put("台州", 331000);
		cityCodeMap.put("丽水", 331100);
		cityCodeMap.put("合肥", 340100);
		cityCodeMap.put("芜湖", 340200);
		cityCodeMap.put("蚌埠", 340300);
		cityCodeMap.put("淮南", 340400);
		cityCodeMap.put("马鞍山", 340500);
		cityCodeMap.put("淮北", 340600);
		cityCodeMap.put("铜陵", 340700);
		cityCodeMap.put("安庆", 340800);
		cityCodeMap.put("黄山", 341000);
		cityCodeMap.put("滁州", 341100);
		cityCodeMap.put("阜阳", 341200);
		cityCodeMap.put("宿州", 341300);
		cityCodeMap.put("巢湖", 341400);
		cityCodeMap.put("六安", 341500);
		cityCodeMap.put("亳州", 341600);
		cityCodeMap.put("池州", 341700);
		cityCodeMap.put("宣城", 341800);
		cityCodeMap.put("福州", 350100);
		cityCodeMap.put("厦门", 350200);
		cityCodeMap.put("莆田", 350300);
		cityCodeMap.put("三明", 350400);
		cityCodeMap.put("泉州", 350500);
		cityCodeMap.put("漳州", 350600);
		cityCodeMap.put("南平", 350700);
		cityCodeMap.put("龙岩", 350800);
		cityCodeMap.put("宁德", 350900);
		cityCodeMap.put("南昌", 360100);
		cityCodeMap.put("景德镇", 360200);
		cityCodeMap.put("萍乡", 360300);
		cityCodeMap.put("九江", 360400);
		cityCodeMap.put("新余", 360500);
		cityCodeMap.put("鹰潭", 360600);
		cityCodeMap.put("赣州", 360700);
		cityCodeMap.put("吉安", 360800);
		cityCodeMap.put("宜春", 360900);
		cityCodeMap.put("抚州", 361000);
		cityCodeMap.put("上饶", 361100);
		cityCodeMap.put("济南", 370100);
		cityCodeMap.put("青岛", 370200);
		cityCodeMap.put("淄博", 370300);
		cityCodeMap.put("枣庄", 370400);
		cityCodeMap.put("东营", 370500);
		cityCodeMap.put("烟台", 370600);
		cityCodeMap.put("潍坊", 370700);
		cityCodeMap.put("济宁", 370800);
		cityCodeMap.put("泰安", 370900);
		cityCodeMap.put("威海", 371000);
		cityCodeMap.put("日照", 371100);
		cityCodeMap.put("莱芜", 371200);
		cityCodeMap.put("临沂", 371300);
		cityCodeMap.put("德州", 371400);
		cityCodeMap.put("聊城", 371500);
		cityCodeMap.put("滨州", 371600);
		cityCodeMap.put("菏泽", 371700);
		cityCodeMap.put("郑州", 410100);
		cityCodeMap.put("开封", 410200);
		cityCodeMap.put("洛阳", 410300);
		cityCodeMap.put("平顶山", 410400);
		cityCodeMap.put("安阳", 410500);
		cityCodeMap.put("鹤壁", 410600);
		cityCodeMap.put("新乡", 410700);
		cityCodeMap.put("焦作", 410800);
		cityCodeMap.put("濮阳", 410900);
		cityCodeMap.put("许昌", 411000);
		cityCodeMap.put("漯河", 411100);
		cityCodeMap.put("三门峡", 411200);
		cityCodeMap.put("南阳", 411300);
		cityCodeMap.put("商丘", 411400);
		cityCodeMap.put("信阳", 411500);
		cityCodeMap.put("周口", 411600);
		cityCodeMap.put("驻马店", 411700);
		cityCodeMap.put("武汉", 420100);
		cityCodeMap.put("黄石", 420200);
		cityCodeMap.put("十堰", 420300);
		cityCodeMap.put("宜昌", 420500);
		cityCodeMap.put("襄樊", 420600);
		cityCodeMap.put("鄂州", 420700);
		cityCodeMap.put("荆门", 420800);
		cityCodeMap.put("孝感", 420900);
		cityCodeMap.put("荆州", 421000);
		cityCodeMap.put("黄冈", 421100);
		cityCodeMap.put("咸宁", 421200);
		cityCodeMap.put("随州", 421300);
		cityCodeMap.put("恩施", 422800);
		//		cityCodeMap.put("省直辖行政单位" , 429000);
		cityCodeMap.put("长沙", 430100);
		cityCodeMap.put("株洲", 430200);
		cityCodeMap.put("湘潭", 430300);
		cityCodeMap.put("衡阳", 430400);
		cityCodeMap.put("邵阳", 430500);
		cityCodeMap.put("岳阳", 430600);
		cityCodeMap.put("常德", 430700);
		cityCodeMap.put("张家界", 430800);
		cityCodeMap.put("益阳", 430900);
		cityCodeMap.put("郴州", 431000);
		cityCodeMap.put("永州", 431100);
		cityCodeMap.put("怀化", 431200);
		cityCodeMap.put("娄底", 431300);
		cityCodeMap.put("湘西", 433100);
		cityCodeMap.put("广州", 440100);
		cityCodeMap.put("韶关", 440200);
		cityCodeMap.put("深圳", 440300);
		cityCodeMap.put("珠海", 440400);
		cityCodeMap.put("汕头", 440500);
		cityCodeMap.put("佛山", 440600);
		cityCodeMap.put("江门", 440700);
		cityCodeMap.put("湛江", 440800);
		cityCodeMap.put("茂名", 440900);
		cityCodeMap.put("肇庆", 441200);
		cityCodeMap.put("惠州", 441300);
		cityCodeMap.put("梅州", 441400);
		cityCodeMap.put("汕尾", 441500);
		cityCodeMap.put("河源", 441600);
		cityCodeMap.put("阳江", 441700);
		cityCodeMap.put("清远", 441800);
		cityCodeMap.put("东莞", 441900);
		cityCodeMap.put("中山", 442000);
		cityCodeMap.put("石岐", 442100);
		cityCodeMap.put("东区", 442200);
		cityCodeMap.put("火炬", 442300);
		cityCodeMap.put("西区", 442400);
		cityCodeMap.put("南区", 442500);
		//		cityCodeMap.put("五桂山" , 442600);
		cityCodeMap.put("潮州", 445100);
		cityCodeMap.put("揭阳", 445200);
		cityCodeMap.put("云浮", 445300);
		cityCodeMap.put("南宁", 450100);
		cityCodeMap.put("柳州", 450200);
		cityCodeMap.put("桂林", 450300);
		cityCodeMap.put("梧州", 450400);
		cityCodeMap.put("北海", 450500);
		cityCodeMap.put("防城港", 450600);
		cityCodeMap.put("钦州", 450700);
		cityCodeMap.put("贵港", 450800);
		cityCodeMap.put("玉林", 450900);
		cityCodeMap.put("百色", 451000);
		cityCodeMap.put("贺州", 451100);
		cityCodeMap.put("河池", 451200);
		cityCodeMap.put("来宾", 451300);
		cityCodeMap.put("崇左", 451400);
		cityCodeMap.put("海口", 460100);
		cityCodeMap.put("三亚", 460200);
		//		cityCodeMap.put("省直辖县级行政区划" , 469000);
		cityCodeMap.put("重庆", 500100);
		//		cityCodeMap.put("县" , 500200);
		cityCodeMap.put("成都", 510100);
		cityCodeMap.put("自贡", 510300);
		cityCodeMap.put("攀枝花", 510400);
		cityCodeMap.put("泸州", 510500);
		cityCodeMap.put("德阳", 510600);
		cityCodeMap.put("绵阳", 510700);
		cityCodeMap.put("广元", 510800);
		cityCodeMap.put("遂宁", 510900);
		cityCodeMap.put("内江", 511000);
		cityCodeMap.put("乐山", 511100);
		cityCodeMap.put("南充", 511300);
		cityCodeMap.put("眉山", 511400);
		cityCodeMap.put("宜宾", 511500);
		cityCodeMap.put("广安", 511600);
		cityCodeMap.put("达州", 511700);
		cityCodeMap.put("雅安", 511800);
		cityCodeMap.put("巴中", 511900);
		cityCodeMap.put("资阳", 512000);
		cityCodeMap.put("阿坝", 513200);
		cityCodeMap.put("甘孜", 513300);
		cityCodeMap.put("凉山", 513400);
		cityCodeMap.put("贵阳", 520100);
		cityCodeMap.put("六盘水", 520200);
		cityCodeMap.put("遵义", 520300);
		cityCodeMap.put("安顺", 520400);
		cityCodeMap.put("铜仁", 522200);
		cityCodeMap.put("黔西", 522300);
		cityCodeMap.put("毕节", 522400);
		cityCodeMap.put("黔东南", 522600);
		cityCodeMap.put("黔南", 522700);
		cityCodeMap.put("昆明", 530100);
		cityCodeMap.put("曲靖", 530300);
		cityCodeMap.put("玉溪", 530400);
		cityCodeMap.put("保山", 530500);
		cityCodeMap.put("昭通", 530600);
		cityCodeMap.put("丽江", 530700);
		cityCodeMap.put("思茅", 530800);
		cityCodeMap.put("临沧", 530900);
		cityCodeMap.put("楚雄", 532300);
		cityCodeMap.put("红河", 532500);
		cityCodeMap.put("文山", 532600);
		cityCodeMap.put("西双版纳", 532800);
		cityCodeMap.put("大理", 532900);
		cityCodeMap.put("德宏", 533100);
		cityCodeMap.put("怒江", 533300);
		cityCodeMap.put("迪庆", 533400);
		cityCodeMap.put("拉萨", 540100);
		cityCodeMap.put("昌都", 542100);
		cityCodeMap.put("山南", 542200);
		cityCodeMap.put("日喀则", 542300);
		cityCodeMap.put("那曲", 542400);
		cityCodeMap.put("阿里", 542500);
		cityCodeMap.put("林芝", 542600);
		cityCodeMap.put("西安", 610100);
		cityCodeMap.put("铜川", 610200);
		cityCodeMap.put("宝鸡", 610300);
		cityCodeMap.put("咸阳", 610400);
		cityCodeMap.put("渭南", 610500);
		cityCodeMap.put("延安", 610600);
		cityCodeMap.put("汉中", 610700);
		cityCodeMap.put("榆林", 610800);
		cityCodeMap.put("安康", 610900);
		cityCodeMap.put("商洛", 611000);
		cityCodeMap.put("兰州", 620100);
		cityCodeMap.put("嘉峪关", 620200);
		cityCodeMap.put("金昌", 620300);
		cityCodeMap.put("白银", 620400);
		cityCodeMap.put("天水", 620500);
		cityCodeMap.put("武威", 620600);
		cityCodeMap.put("张掖", 620700);
		cityCodeMap.put("平凉", 620800);
		cityCodeMap.put("酒泉", 620900);
		cityCodeMap.put("庆阳", 621000);
		cityCodeMap.put("定西", 621100);
		cityCodeMap.put("陇南", 621200);
		cityCodeMap.put("临夏", 622900);
		cityCodeMap.put("甘南", 623000);
		cityCodeMap.put("西宁", 630100);
		cityCodeMap.put("海东", 632100);
		cityCodeMap.put("海北", 632200);
		cityCodeMap.put("黄南", 632300);
		cityCodeMap.put("海南", 632500);
		cityCodeMap.put("果洛", 632600);
		cityCodeMap.put("玉树", 632700);
		cityCodeMap.put("海西", 632800);
		cityCodeMap.put("银川", 640100);
		cityCodeMap.put("石嘴山", 640200);
		cityCodeMap.put("吴忠", 640300);
		cityCodeMap.put("固原", 640400);
		cityCodeMap.put("中卫", 640500);
		cityCodeMap.put("乌鲁木齐", 650100);
		cityCodeMap.put("克拉玛依", 650200);
		cityCodeMap.put("吐鲁番", 652100);
		cityCodeMap.put("哈密", 652200);
		cityCodeMap.put("昌吉", 652300);
		cityCodeMap.put("博尔塔拉", 652700);
		cityCodeMap.put("巴音郭楞", 652800);
		cityCodeMap.put("阿克苏", 652900);
		cityCodeMap.put("克", 653000);
		cityCodeMap.put("喀什", 653100);
		cityCodeMap.put("和田", 653200);
		cityCodeMap.put("伊犁", 654000);
		cityCodeMap.put("塔城", 654200);
		cityCodeMap.put("阿勒泰", 654300);
		//		cityCodeMap.put("省直辖行政单位" , 659000);
	}

	@Deprecated
	public static Integer getProviceCode(String provice) {
		return getProvCode(provice);
	}

	/**
	 * 获取省编码
	 *  
	 * @author qiuxs  
	 * @param provice
	 * @return
	 */
	public static Integer getProvCode(String provice) {
		return provCodeMap.get(provice);
	}

	/**
	 * 获取市编码
	 *  
	 * @author qiuxs  
	 * @param city
	 * @return
	 */
	public static Integer getCityCode(String city) {
		return cityCodeMap.get(city);
	}

	public static Integer getProvCodeByRandom() {
		int rand = RandomGenerator.getRandomInt(provCodeList.size());
		return provCodeList.get(rand);
	}

	public static Integer getCityCodeByRandom(Integer provCode) {
		List<Integer> cityCodes = provCodeCityCodesMap.get(provCode);
		if (CollectionUtils.isNotEmpty(cityCodes)) {
			int rand = RandomGenerator.getRandomInt(cityCodes.size());
			return cityCodes.get(rand);
		}
		return null;
	}

	public static Pair<Integer, Integer> getProvCodeCityCodeByIp(String ip) {
		return Ip2RegionFacade.getProvCodeCityCode(ip);
	}

	/**
	 * 1. 按手机号查省市
	 * 2. 查询不到按ip地址查省市
	 * 3. 还是查不到，随时省市
	 *  
	 * @author qiuxs  
	 * @param ip
	 * @return
	 */
	public static Pair<Integer, Integer> getProvCodeCityCodeByMobileIpRandom(String ip) {
		Pair<Integer, Integer> pair = Ip2RegionFacade.getProvCodeCityCode(ip);
		if (pair == null) {
			Integer provCode = RegionConstants.getProvCodeByRandom();
			Integer cityCode = RegionConstants.getCityCodeByRandom(provCode);
			pair = new Pair<Integer, Integer>(provCode, cityCode);
		}
		return pair;
	}

}
