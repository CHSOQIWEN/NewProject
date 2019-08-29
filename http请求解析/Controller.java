package http请求解析;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/29 10:13
 */
//controller类比于HttpServlet
public  abstract class Controller {
    //使用抽象类的原因：为了实现四种方法？？？？？？？
    /*Controller生命周期：一次调用init和destory，多次调用doget和dopost*/
    /*doget和dopost默认写405响应*/
    /*真正的业务controller全部继承controller,覆写doget/dopost请求*/


    /*3种controller：
    * 1、StaticController  静态文件（内置controller）
    * 2、动态的controller，业务自定义controller
    *    博客的列表页
    * 3、JSP controller （内置controller）
    * */




}
