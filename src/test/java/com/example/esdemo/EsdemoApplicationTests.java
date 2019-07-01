package com.example.esdemo;

import com.example.esdemo.dao.ArticleSearchRepository;
import com.example.esdemo.dao.DocRepository;
import com.example.esdemo.dao.RedisUtils;
import com.example.esdemo.entity.Article;
import com.example.esdemo.entity.Doc;
import com.example.esdemo.lib.validation.AdvancedSearchValidation;
import com.example.esdemo.service.DocSearchService;

import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsdemoApplicationTests {

    @Test
    public void contextLoads() {
    }

//    @Autowired
//    public ElasticsearchTemplate elasticsearchTemplate;
//
//    @Autowired
//    public ArticleSearchRepository articleSearchRepository;
//
//    @Autowired
//    public DocRepository docDao;
//
//    @Autowired
//    public DocSearchService docSearchService;
//
//    @Autowired
//    public RedisUtils redisUtils;
//
//    private JSONObject json = new JSONObject();
//
//
//    @Test
//    public void contextLoads() {
//    }
//
//    @Test
//    public void testcreateindex(){
//        elasticsearchTemplate.createIndex(Doc.class);
//    }
//    @Test
//    public void testMapindex(){
////        elasticsearchTemplate.putMapping(Doc.class);
////        elasticsearchTemplate.deleteIndex("doc");
//    }
//
//    @Test
//    public void testDeleteDoc(){
//        Doc doc = docDao.findById(2l);
//        docDao.delete(doc);
//    }
//
//    @Test
//    public void testSearch(){
//        List<Doc> list = docDao.queryByContent("第定");
//        list.forEach(a -> System.out.println(a));
//    }
//
//    @Test
//    public void testAdd() throws Exception{
//        Doc doc = new Doc();
//        doc.setId(1);
//        doc.setCaseName("赵龙抢劫死刑复核刑事裁定书");
//        List<String> typeList = new ArrayList<>();
//        typeList.add("刑事");
//        typeList.add("裁定");
//        doc.setCaseType(typeList);
//        doc.setCourt("最高人民法院");
//        doc.setDocType("刑事裁定书");
//        List<String> litigantList = new ArrayList<>();
//        litigantList.add("樊明");
//        litigantList.add("刘希龙");
//        doc.setLitigant(litigantList);
//        doc.setContent("被告人赵龙,又名赵杰，男，汉族，1986年5月25日出生于内蒙古自治区卓资县，高中文化，无业。2007年9月29日被逮捕。现在押。\n" +
//                "内蒙古自治区乌兰察布市中级人民法院审理乌兰察布市人民检察院指控被告人赵龙犯抢劫罪、故意杀人罪一案，于2008年8月6日以（2008）乌刑初字第38号刑事判决，认定被告人赵龙犯抢劫罪，判处死刑，剥夺政治权利终身，并处没收个人全部财产。宣判后，赵龙提出上诉。内蒙古自治区高级人民法院于2009年8月28日作出（2009）内刑一终字第94号刑事裁定，以违反法律规定的诉讼程序为由，撤销原判，发回重审。乌兰察布市中级人民法院经依法重新审理，于2010年1月12日以（2009）乌刑初字第49号刑事附带民事判决，认定被告人赵龙犯抢劫罪，判处死刑，剥夺政治权利终身，并处没收个人全部财产。宣判后，赵龙提出上诉。内蒙古自治区高级人民法院于2010年7月16日作出（2010）内刑一复字第109号刑事附带民事裁定，再次以违反法律规定的诉讼程序为由，撤销原判，发回重审。乌兰察布市中级人民法院经依法重新审理，于2011年1月24日以（2010）乌刑初字第47号刑事附带民事判决，认定被告人赵龙犯抢劫罪，判处死刑，剥夺政治权利终身，并处没收个人全部财产。宣判后，赵龙提出上诉。内蒙古自治区高级人民法院经依法开庭审理，于2012年9月21日以（2011）内刑一复字第58号刑事附带民事判决，维持原审对被告人赵龙的刑事判决，并依法报请本院核准。本院依法组成合议庭，对本案进行了复核，依法讯问了被告人。现已复核终结。\n" +
//                "经复核确认：2006年12月17日，被告人赵龙向吕俊、冯晓东、巩某某（均系同案被告人，已判刑）提议抢劫，吕、冯、巩均表示同意，为此赵龙、吕俊、冯晓东各准备了一把尖刀。当晚，赵龙等人乘坐巩某某驾驶的牌号为蒙J330**的一汽佳宝牌面包车寻找抢劫目标未果，赵龙决定以购买毒品为名到被害人王某某（男，殁年27岁）、邢某甲（女，殁年26岁）夫妇家中抢劫，吕俊、冯晓东、巩某某同意。次日零时许，赵龙等人来到王某某家附近。按照赵龙的安排，巩某某在车内望风，赵龙、吕俊、冯晓东各持一把尖刀到王某某家门口。赵龙敲门谎称购买毒品，王某某开门后，赵龙持刀捅刺王的腹部，又与冯晓东一起捅刺王的头部、颈部、胸部等处多刀，其间赵、冯的手均被刀划伤。邢某甲闻声出来查看，吕俊持刀将邢按倒在里屋的床上。赵龙、冯晓东将王某某拖进里屋，见王仍有呼吸，赵龙又指使吕俊捅刺王数刀。随后，赵龙将刀架在邢某甲颈部逼问财物的存放地点，邢某甲告知后，赵龙用力切割邢某甲颈部，并与吕俊、冯晓东捅刺邢的头部、颈部等处多刀。王某某、邢某甲分别因左颈动脉、左颈静脉被刺破引起大失血死亡。赵龙、吕俊、冯晓东劫取王某某家中的现金1万元及首饰、手机、纪念币、银行卡等财物，后逃离现场。\n" +
//                "上述事实，有第一审、第二审开庭审理中经质证确认的作案工具面包车、赃物纪念币7枚等物证的照片，证人邢某乙、穆某某、范某某、赵某等的证言，尸体鉴定意见、证实现场提取的3处血迹为被告人赵龙所留、2处血迹为同案被告人冯晓东所留的DNA鉴定意见，现场勘验、检查笔录，同案被告人巩某某、冯晓东、吕俊的供述等证据证实。被告人赵龙亦供认。足以认定。\n" +
//                "此外，2006年11月11日，被告人赵龙与吕俊、冯晓东经预谋，由赵龙驾驶从巩某某处借来的牌号为蒙J330**的一汽佳宝牌面包车，载吕俊、冯晓东在乌兰察布市集宁区寻找抢劫目标。次日1时许，赵龙等人在集宁区兴工路见被害人王甲（又名王乙，男，时年31岁）独自行走，即拦住王甲，用砖块将其殴打后抢走其现金5000元。\n" +
//                "上述事实，有第一审、第二审开庭审理中经质证确认的作案工具面包车的照片，被害人王甲的陈述，同案被告人巩某某、冯晓东、吕俊的供述等证据证实。被告人赵龙亦供认。足以认定。\n" +
//                "本院认为，被告人赵龙伙同他人以非法占有为目的，采用暴力手段强行劫取被害人财物，其行为已构成抢劫罪。赵龙提议抢劫，率先实施暴力，伙同他人共同致死被害人，在共同犯罪中起主要作用，系主犯，应当按照其所参与的全部犯罪处罚。赵龙伙同他人抢劫2起，入户抢劫致2人死亡，犯罪性质特别恶劣，手段残忍，社会危害大，后果和罪行极其严重，应依法惩处。第一审、第二审判决认定的事实清楚，证据确实、充分，定罪准确，对赵龙量刑适当。审判程序合法。依照《中华人民共和国刑事诉讼法》第二百三十五条、第二百三十九条和《最高人民法院关于适用〈中华人民共和国刑事诉讼法〉的解释》第三百五十条第（一）项的规定，裁定如下：\n" +
//                "核准内蒙古自治区高级人民法院（2011）内刑一复字第58号维持第一审以抢劫罪判处被告人赵龙死刑，剥夺政治权利终身，并处没收个人全部财产的刑事附带民事判决。\n" +
//                "本裁定自宣告之日起发生法律效力。\n" +
//                "审　判　长　　李　彤\n" +
//                "代理审判员　　王海波\n" +
//                "代理审判员　　章　政\n" +
//                "\n" +
//                "二〇一三年十一月二十九日\n" +
//                "书　记　员　　张　妍");
//        doc.setReason("故意杀人罪");
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
//        doc.setTime(sdf.parse("2013.11.23"));
//        doc.setCaseNumber("《最高人民法院公报》 1985年第1号");
//        List<String> stageList = new ArrayList<>();
//        stageList.add("一审");
//        stageList.add("二审");
//        doc.setStage(stageList);
//        List<String> lawList = new ArrayList<>();
//        lawList.add("《中华人民共和国侵权责任法》第八条");
//        lawList.add("《中华人民共和国侵权责任法》第十条");
//        doc.setLawList(lawList);
//        doc.setUrl("https://www.jufaanli.com/wenshu/8396520f8bae7b56e98f3b919c8c228b/?q=%E6%9D%80%E4%BA%BA&src=search");
//
//        docDao.save(doc);
//
//    }
//
//    @Test
//    public void testAddDoc2() throws Exception{
//        Doc doc = new Doc();
//        doc.setId(2);
//        doc.setCaseName("孙明亮故意伤害二审案");
//        List<String> typeList = new ArrayList<>();
//        typeList.add("刑事");
//        typeList.add("判决");
//        doc.setCaseType(typeList);
//        doc.setCourt("甘肃省高级人民法院");
//        doc.setDocType("刑事判决书");
//        List<String> litigantList = new ArrayList<>();
//        litigantList.add("孙明亮");
//        doc.setLitigant(litigantList);
//        doc.setContent("当事人信息\n" +
//                "被告人孙明亮。\n" +
//                "\n" +
//                "一九八四年六月二十五日晚八时许，被告人孙明亮偕同其友蒋小平去看电影，在平凉市东关电影院门口，看到郭鹏祥及郭小平、马忠全三人尾追少女陈××、张××，郭鹏祥对陈××撕拉纠缠。孙明亮和蒋小平上前制止，与郭鹏祥等三人发生争执。争执中，蒋小平动手打了郭鹏祥面部一拳，郭鹏祥等三人即分头逃跑，孙明亮和蒋小平分别追赶不及，遂返回将陈××、张××护送回家。此时，郭小平、马忠全到平凉市运输公司院内叫来正在看电影的胡维革、班保存等六人，与郭鹏祥会合后，结伙寻找孙明亮、蒋小平，企图报复。当郭鹏祥等九人在一小巷内发现孙明亮、蒋小平二人后，即将孙明亮、蒋小平二人拦截住。郭小平手执半块砖头，郭鹏祥上前质问孙明亮、蒋小平为啥打人。蒋小平反问：人家女子年龄那么小，你们黑天半夜缠着干啥？并佯称少女陈××是自己的妹妹。郭鹏祥听后，即照蒋小平面部猛击一拳。蒋小平挨打后与孙明亮退到附近街墙旁一垃圾堆上。郭鹏祥追上垃圾堆继续扑打，孙明亮掏出随身携带的弹簧刀（孙明亮系郊区菜农，因晚上在菜地看菜，在市场上买来此刀防身），照迎面扑来的郭鹏祥左胸刺了一刀，郭鹏祥当即跌倒。孙明亮又持刀对空乱抡几下，与蒋小平乘机脱身跑掉。郭鹏祥因被刺伤左肺、胸膜、心包膜、肺动脉等器官，失血过多，于送往医院途中死亡。\n" +
//                "\n" +
//                "审理经过\n" +
//                "一九八四年十月七日，甘肃省平凉地区人民检察分院以故意杀人罪对被告人孙明亮提起公诉。平凉地区中级人民法院依法组成合议庭，对该案进行公开审理，认定孙明亮在打架斗殴中，持刀伤害他人致死，后果严重，犯有《中华人民共和国刑法》第一百三十四条故意伤害罪，依照该条二款的规定，于一九八四年十一月二十三日判处孙明亮有期徒刑十五年。\n" +
//                "\n" +
//                "宣判后，被告人孙明亮不上诉。平凉地区人民检察分院以第一审判决定罪不准，量刑失轻为由，依照《中华人民共和国刑事诉讼法》第一百三十条和第一百三十三条一款的规定，于一九八四年十二月四日向甘肃省高级人民法院起出抗诉，并将抗诉书副本抄送甘肃省人民检察院。平凉地区人民检察分院认为：一、孙明亮在打架斗殴中，对用刀刺人会造成被刺人死亡或者受伤的后果是清楚的，但在其主观上对两种后果的发生，均持放任的态度。在这种情况下，是定（间接）故意伤害罪还是（间接）故意杀人罪？应以实际造成的后果来确定。鉴于郭鹏祥已死亡，应定（间接）故意杀人罪。第一审判决对孙明亮定（间接）故意伤害罪不当。二、孙明亮持刀致人死亡，造成严重后果，无论以故意伤害罪还是故意杀人罪，判处有期徒刑十五年均失轻。全国人大常委会《关于严惩严重危害社会治安的犯罪分子的决定》对刑法第一百三十四条作了补充，规定对故意伤害致人死亡的，可以在刑法规定的最高刑以上处刑、直至判处死刑，其精神在于对持刀行凶者，要予以严惩。刑法第一百三十二条对故意杀人罪处刑规定的精神是：故意杀人的，首先应考虑处死刑，其次是无期徒刑，然后才是有期徒刑。因此，对孙明亮判处十五年有期徒刑，不符合上述法律规定的精神。\n" +
//                "\n" +
//                "甘肃省高级人民法院依照刑事诉讼法第二审程序对该案进行第二审。在审理中，发现第一审判决适用法律有错误。与此同时，甘肃省人民检察院调卷审查平凉地区人民检察分院的抗诉，并于一九八五年一月二十八日经检察委员会讨论，认为：孙明亮的行为属于防卫过当，第一审判处十五年有期徒刑失重；平凉地区人民检察分院以定罪不准、量刑失轻为由抗诉不当。决定依照刑事诉讼法第一百三十三条二款的规定，向甘肃省高级人民法院撤回抗诉。\n" +
//                "\n" +
//                "一审法院认为\n" +
//                "由于抗诉撤回后，第一审判决已发生法律效力，甘肃省高级人民法院依照刑事诉讼法第一百四十九条二款的规定，决定提审该案。一九八五年三月二十七日经该院审判委员会讨论，认为第一审判决对孙明亮行为的性质认定和在适用刑罚上，均有不当。孙明亮及其友蒋小平路遇郭鹏祥等人在公共场所对少女实施流氓行为时，予以制止，虽与郭鹏祥等人发生争执，蒋小平动手打了郭鹏祥一拳，但并非流氓分子之间的打架斗殴，而是公民积极同违法犯罪行为作斗争的正义行为，应予以肯定和支持。郭鹏祥等人不听规劝，反而纠结多人拦截孙明亮和蒋小平进行报复，其中郭小平手持砖块与同伙一起助威，郭鹏祥主动进攻，对蒋小平实施不法侵害。蒋小平挨打后，与孙明亮退到垃圾堆上，郭鹏祥仍继续扑打。孙明亮在自己和蒋小平已无后退之路的情况下，为了免遭正在进行的不法侵害，持刀进行还击，其行为属正当防卫，是合法的。但是，由于郭鹏祥是徒手实施不法侵害，郭小平手持砖头与同伙一起助威，孙明亮在这种情况下，持刀将郭鹏祥刺伤致死，其正当防卫行为超过必要的限度，造成不应有的危害后果，属于防卫过当，构成故意伤害罪。依照刑法第七十条二款的规定，应当负刑事责任；但应当在刑法第一百三十四条二款规定的法定刑以下减轻处罚。第一审判决未考虑这一情节，量刑畸重，应予纠正。据此，甘肃省高级人民法院判决如下：\n" +
//                "\n" +
//                "撤销第一审判决，以故意伤害罪改判被告人孙明亮有期徒刑二年，缓刑三年。\n" +
//                "\n" +
//                "本院查明\n" +
//                "最高人民法院审判委员会一九八五年六月五日第二百二十六次会议，依照《中华人民共和国人民法院组织法》第十一条一款的规定，在总结审判经验时认为，对于公民自觉地与违法犯罪行为作斗争，应当予以支持和保护。人民法院在审判工作中，要注意把公民在遭受不法侵害而进行正当防卫时的防卫过当行为，与犯罪分子主动实施的犯罪行为区别开来，做到既惩罚犯罪，又支持正义行为。甘肃省高级人民法院对该案的提审判决，正确认定了孙明亮的行为的性质，且适用法律得当，审判程序合法，可供各级人民借鉴。");
//        doc.setReason("故意伤害罪");
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        doc.setTime(sdf.parse("1985-03-27"));
//        doc.setCaseNumber("《最高人民法院公报》 1985年第2号");
//        List<String> stageList = new ArrayList<>();
//        stageList.add("二审");
//        doc.setStage(stageList);
//        List<String> lawList = new ArrayList<>();
//        lawList.add("《中华人民共和国侵权责任法》第八条");
//        lawList.add("《中华人民共和国侵权责任法》第二十六条");
//        doc.setLawList(lawList);
//        doc.setUrl("https://www.jufaanli.com/wenshu/316ffe22bebb7ff8596143a8a34cb851/?q=%E6%9D%80%E4%BA%BA&src=search");
//
//        docDao.save(doc);
//    }
//
//    @Test
//    public void testAggregation(){
//        Object reason = docSearchService.agregationSearch("reason",false);
//        System.out.println(reason);
//
//    }
//
//    @Test
//    public void testRedis(){
//        Doc doc = new Doc();
//        doc.setCaseNumber("hh");
//        redisUtils.set("ke", doc);
//        Doc doc2 = (Doc) redisUtils.get("ke");
//        System.out.println(doc2.getCaseName());
//    }
//
//    @Test
//    public void testRedisIncr(){
//        redisUtils.set("incr",1);
//        System.out.println(redisUtils.incr("incr",1));
//    }
//
//    @Test
//    public void testData(){
//
//        System.out.println(new Date().getTime());
//    }
//
//    @Test
//    public void testAnalyse(){
//        AdvancedSearchValidation ad = new AdvancedSearchValidation();
//        ad.setDocType("刑事判决书");
//        List<Map<String,String>> mapList = new ArrayList<>();
//        Map<String,String> docType = new HashMap<>();
//        docType.put("key","docType");
//        docType.put("order","false");
//
//        Map<String,String> caseType = new HashMap<>();
//        caseType.put("key","caseType");
//        caseType.put("order","false");
//
//        Map<String,String> reason = new HashMap<>();
//        reason.put("key","reason");
//        reason.put("order","false");
//
//        Map<String,String> stage = new HashMap<>();
//        stage.put("key","stage");
//        stage.put("order","false");
//
//
//        mapList.add(docType);
//        mapList.add(caseType);
//        mapList.add(reason);
//        mapList.add(stage);
//
//        Object res = docSearchService.searchResultAnalyse(ad,mapList);
//        System.out.println(res);
//    }
//
//    @Test
//    public void testRecomendByLike(){
//        Object res = docSearchService.recomendByLike(1561627089945l);
//        System.out.println(res);
//    }
}

