package com.example.esdemo;

import com.example.esdemo.dao.ArticleSearchRepository;
import com.example.esdemo.dao.DocRepository;
import com.example.esdemo.entity.Article;
import com.example.esdemo.entity.Doc;
import com.example.esdemo.service.DocSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsdemoApplicationTests {

    @Autowired
    public ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public ArticleSearchRepository articleSearchRepository;

    @Autowired
    public DocRepository docDao;

    @Autowired
    public DocSearchService docSearchService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testcreateindex(){
        elasticsearchTemplate.createIndex(Doc.class);
    }
    @Test
    public void testMapindex(){
        elasticsearchTemplate.deleteIndex("doc");
    }

    @Test
    public void testSearch(){
        List<Doc> list = docDao.queryByContent("第定");
        list.forEach(a -> System.out.println(a));
    }

    @Test
    public void testAdd() throws Exception{
        Doc doc = new Doc();
        doc.setId(1);
        doc.setCaseName("赵龙抢劫死刑复核刑事裁定书");
        doc.setCaseType("刑事");
        doc.setCourt("最高人民法院");
        doc.setDocType("刑事裁定书");
        doc.setLawyer("李彤");
        doc.setLitigant("赵龙");
        doc.setContent("被告人赵龙,又名赵杰，男，汉族，1986年5月25日出生于内蒙古自治区卓资县，高中文化，无业。2007年9月29日被逮捕。现在押。\n" +
                "内蒙古自治区乌兰察布市中级人民法院审理乌兰察布市人民检察院指控被告人赵龙犯抢劫罪、故意杀人罪一案，于2008年8月6日以（2008）乌刑初字第38号刑事判决，认定被告人赵龙犯抢劫罪，判处死刑，剥夺政治权利终身，并处没收个人全部财产。宣判后，赵龙提出上诉。内蒙古自治区高级人民法院于2009年8月28日作出（2009）内刑一终字第94号刑事裁定，以违反法律规定的诉讼程序为由，撤销原判，发回重审。乌兰察布市中级人民法院经依法重新审理，于2010年1月12日以（2009）乌刑初字第49号刑事附带民事判决，认定被告人赵龙犯抢劫罪，判处死刑，剥夺政治权利终身，并处没收个人全部财产。宣判后，赵龙提出上诉。内蒙古自治区高级人民法院于2010年7月16日作出（2010）内刑一复字第109号刑事附带民事裁定，再次以违反法律规定的诉讼程序为由，撤销原判，发回重审。乌兰察布市中级人民法院经依法重新审理，于2011年1月24日以（2010）乌刑初字第47号刑事附带民事判决，认定被告人赵龙犯抢劫罪，判处死刑，剥夺政治权利终身，并处没收个人全部财产。宣判后，赵龙提出上诉。内蒙古自治区高级人民法院经依法开庭审理，于2012年9月21日以（2011）内刑一复字第58号刑事附带民事判决，维持原审对被告人赵龙的刑事判决，并依法报请本院核准。本院依法组成合议庭，对本案进行了复核，依法讯问了被告人。现已复核终结。\n" +
                "经复核确认：2006年12月17日，被告人赵龙向吕俊、冯晓东、巩某某（均系同案被告人，已判刑）提议抢劫，吕、冯、巩均表示同意，为此赵龙、吕俊、冯晓东各准备了一把尖刀。当晚，赵龙等人乘坐巩某某驾驶的牌号为蒙J330**的一汽佳宝牌面包车寻找抢劫目标未果，赵龙决定以购买毒品为名到被害人王某某（男，殁年27岁）、邢某甲（女，殁年26岁）夫妇家中抢劫，吕俊、冯晓东、巩某某同意。次日零时许，赵龙等人来到王某某家附近。按照赵龙的安排，巩某某在车内望风，赵龙、吕俊、冯晓东各持一把尖刀到王某某家门口。赵龙敲门谎称购买毒品，王某某开门后，赵龙持刀捅刺王的腹部，又与冯晓东一起捅刺王的头部、颈部、胸部等处多刀，其间赵、冯的手均被刀划伤。邢某甲闻声出来查看，吕俊持刀将邢按倒在里屋的床上。赵龙、冯晓东将王某某拖进里屋，见王仍有呼吸，赵龙又指使吕俊捅刺王数刀。随后，赵龙将刀架在邢某甲颈部逼问财物的存放地点，邢某甲告知后，赵龙用力切割邢某甲颈部，并与吕俊、冯晓东捅刺邢的头部、颈部等处多刀。王某某、邢某甲分别因左颈动脉、左颈静脉被刺破引起大失血死亡。赵龙、吕俊、冯晓东劫取王某某家中的现金1万元及首饰、手机、纪念币、银行卡等财物，后逃离现场。\n" +
                "上述事实，有第一审、第二审开庭审理中经质证确认的作案工具面包车、赃物纪念币7枚等物证的照片，证人邢某乙、穆某某、范某某、赵某等的证言，尸体鉴定意见、证实现场提取的3处血迹为被告人赵龙所留、2处血迹为同案被告人冯晓东所留的DNA鉴定意见，现场勘验、检查笔录，同案被告人巩某某、冯晓东、吕俊的供述等证据证实。被告人赵龙亦供认。足以认定。\n" +
                "此外，2006年11月11日，被告人赵龙与吕俊、冯晓东经预谋，由赵龙驾驶从巩某某处借来的牌号为蒙J330**的一汽佳宝牌面包车，载吕俊、冯晓东在乌兰察布市集宁区寻找抢劫目标。次日1时许，赵龙等人在集宁区兴工路见被害人王甲（又名王乙，男，时年31岁）独自行走，即拦住王甲，用砖块将其殴打后抢走其现金5000元。\n" +
                "上述事实，有第一审、第二审开庭审理中经质证确认的作案工具面包车的照片，被害人王甲的陈述，同案被告人巩某某、冯晓东、吕俊的供述等证据证实。被告人赵龙亦供认。足以认定。\n" +
                "本院认为，被告人赵龙伙同他人以非法占有为目的，采用暴力手段强行劫取被害人财物，其行为已构成抢劫罪。赵龙提议抢劫，率先实施暴力，伙同他人共同致死被害人，在共同犯罪中起主要作用，系主犯，应当按照其所参与的全部犯罪处罚。赵龙伙同他人抢劫2起，入户抢劫致2人死亡，犯罪性质特别恶劣，手段残忍，社会危害大，后果和罪行极其严重，应依法惩处。第一审、第二审判决认定的事实清楚，证据确实、充分，定罪准确，对赵龙量刑适当。审判程序合法。依照《中华人民共和国刑事诉讼法》第二百三十五条、第二百三十九条和《最高人民法院关于适用〈中华人民共和国刑事诉讼法〉的解释》第三百五十条第（一）项的规定，裁定如下：\n" +
                "核准内蒙古自治区高级人民法院（2011）内刑一复字第58号维持第一审以抢劫罪判处被告人赵龙死刑，剥夺政治权利终身，并处没收个人全部财产的刑事附带民事判决。\n" +
                "本裁定自宣告之日起发生法律效力。\n" +
                "审　判　长　　李　彤\n" +
                "代理审判员　　王海波\n" +
                "代理审判员　　章　政\n" +
                "\n" +
                "二〇一三年十一月二十九日\n" +
                "书　记　员　　张　妍");
        doc.setReason(" ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        doc.setTime(sdf.parse("2013.11.23"));

        docDao.save(doc);

    }

    @Test
    public void testAggregation(){
        docSearchService.subAgregation();

    }
}
