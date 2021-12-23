// 依赖
@Grab("com.alibaba:fastjson:1.2.9")

// 导入包
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import java.math.BigDecimal
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TreeMap

// 程式执行
action(input)

// 主程式
def action(String params) {

    // 执行脚本输入对象
    GroovyExecuteScriptInputDTO input = JSON.parseObject(params, GroovyExecuteScriptInputDTO.class)

    // 渠道服务
    GroovyChannelService service = new GroovyChannelService()

    // 执行方法
    if ("create".equals(input.getScriptMethod())) {
        return service.create(input)
    }
    if ("query".equals(input.getScriptMethod())) {
        return service.query(input)
    }
    if ("parse".equals(input.getScriptMethod())) {
        return service.parse(input)
    }

    return null

}

// 渠道服务
class GroovyChannelService {

    GroovySignUtil groovySignUtil = new GroovySignUtil()
    GroovyHttpUtil groovyHttpUtil = new GroovyHttpUtil()

    // 创建订单
    def create(GroovyExecuteScriptInputDTO input) {

        // 在此处编写创建订单实现
        def bizContentJson = JSON.parseObject(input.getBizContent())

        Map<String, Object> treeMap = new TreeMap<>(String::compareTo)
        treeMap.put("MchId", bizContentJson.channelMerchantId)
        treeMap.put("MchOrderNo", input.getOrderId())
        treeMap.put("NotifyUrl", bizContentJson.callbackUrl)
        treeMap.put("RequestTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(input.getOrderTime()))
        treeMap.put("CategoryCode", bizContentJson.channelCode)
        treeMap.put("Amount", input.getAmount())
        treeMap.put("Attach", "这是附加信息")
        treeMap.put("Title", "这是一笔订单")
        treeMap.put("Sign", groovySignUtil.doSign(treeMap, bizContentJson.channelMerchantKey))

        // Post
        def post = groovyHttpUtil.doPost(bizContentJson.createOrderUrl, JSON.toJSONString(treeMap))
        def json = JSON.parseObject(post)

        // 响应对象
        GroovyExecuteScriptOutputDTO output = new GroovyExecuteScriptOutputDTO()
        // output.setCodeMemberId()    // 渠道方修改后调整此处
        output.setOrderId(input.getOrderId())
        output.setOrderStatus(json.Success ? "NP" : "AF")
        output.setAmount(input.getAmount())
        output.setChannelReturnMessage(json.toJSONString())

        if (json.Success) {

            def resDataJson = json.getJSONObject("ResData")
            output.setCalleeOrderId(resDataJson.OrderNo)
            output.setActualAmount(resDataJson.Amount)

            JSONObject contentJson = new JSONObject();
            contentJson.put("url", resDataJson.CallBackUrl)
            output.setContent(contentJson.toJSONString())

        }
        
        return JSON.toJSONString(output)
    }

    // 调单查询
    def query(GroovyExecuteScriptInputDTO input) {

        // 在此处编写调单查询实现
        def bizContentJson = JSON.parseObject(input.getBizContent())

        Map<String, Object> treeMap = new TreeMap<>(String::compareTo)
        treeMap.put("MchId", bizContentJson.channelMerchantId)
        treeMap.put("MchOrderNo", input.getOrderId())
        treeMap.put("OrderNo", input.getCalleeOrderId())
        treeMap.put("Sign", groovySignUtil.doSign(treeMap, bizContentJson.channelMerchantKey))

        // Post
        def post = groovyHttpUtil.doPost(bizContentJson.queryOrderUrl, JSON.toJSONString(treeMap))
        def json = JSON.parseObject(post)

        // 响应对象
        GroovyExecuteScriptOutputDTO output = new GroovyExecuteScriptOutputDTO()
        output.setOrderId(input.getOrderId())
        output.setCalleeOrderId(input.getCalleeOrderId())
        output.setOrderStatus("AF")
        output.setChannelReturnMessage(json.toJSONString())

        if ("0".equals(json.ErrCode) && 0 == json.ResData) {
            output.setOrderStatus("NP")
            return JSON.toJSONString(output)
        }
        if ("0".equals(json.ErrCode) && 1 == json.ResData) {
            output.setOrderStatus("AP")
            return JSON.toJSONString(output)
        }

        return JSON.toJSONString(output)
    }

    // 渠道回调解析请求参数
    def parse(GroovyExecuteScriptInputDTO input) {

        // 在此处编写渠道回调解析请求参数实现
        def argsJson = JSON.parseObject(input.getArgs())
        def bodyJson = argsJson.getJSONObject("body")

        // 响应对象
        GroovyExecuteScriptOutputDTO output = new GroovyExecuteScriptOutputDTO()
        output.setOrderId(bodyJson.MchOrderNo)
        output.setCalleeOrderId(bodyJson.OrderNo)
        output.setOrderStatus("AP")
        output.setAmount(bodyJson.Amount)
        output.setActualAmount(bodyJson.ActualAmount)
        output.setContent("ok")

        return JSON.toJSONString(output)
    }

}

// Http工具
class GroovyHttpUtil {

    // Post请求
    def doPost(url, data) {
        def conn = new URL(url).openConnection()
        conn.setRequestMethod("POST")
        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8")
        conn.doOutput = true

        def writer = new OutputStreamWriter(conn.outputStream)
        writer.write(data)
        writer.flush()
        writer.close()

        return conn.content.text
    }

}

// 加签工具
class GroovySignUtil {

    // 加签
    def doSign(Map<String, Object> map, String key) {

        def postult = ""

        for (String item : map.keySet()) {

            def value = map.get(item)

            if (null == value) {
                continue
            }

            if (null == postult || "".equals(postult)) {
                postult = item.toLowerCase() + "=" + value
                continue
            }

            postult = postult + "&" + item.toLowerCase() + "=" + value
        }

        postult = postult + "&key=" + key

        return MessageDigest.getInstance("MD5").digest("$postult".bytes).encodeHex().toString().toLowerCase()
    }

}

// 输入对象
class GroovyExecuteScriptInputDTO {

    // 脚本方法
    String scriptMethod

    // 会员编号
    String memberId

    // 订单编号
    String orderId

    // 被调用方订单编号
    String calleeOrderId

    // 订单金额
    BigDecimal amount

    // 订单时间
    Date orderTime

    // 实现路径
    String implPath

    // 业务大字段[{"channelMerchantId":"商户编号","channelMerchantKey":"商户密钥","channelCode":"渠道编号","subChannelCode":"子渠道编号","callbackUrl":"回调地址","redirectUrl":"重定向地址","createOrderUrl":"创建订单地址","queryOrderUrl":"调单查询地址"}]
    String bizContent

    // 参数[{"parameter":{},"header":{},"body":{}}]
    String args

    // 商户客户端IP
    String clientIp

    // 商户重定向地址
    String redirectUrl

}

// 输出对象
class GroovyExecuteScriptOutputDTO {

    // 码商会员编号[自研渠道必须返回]
    String codeMemberId

    // 订单编号
    String orderId

    // 被调用方订单编号
    String calleeOrderId

    // 订单状态
    String orderStatus

    // 订单金额
    BigDecimal amount

    // 实际金额
    BigDecimal actualAmount

    // 网关渠道返回信息
    String channelReturnMessage

    // 报文[作用一:商户下单后返回商户({"text":"html代码","url":"url地址"});作用二:渠道回调时返回渠道(不同渠道的成功标识)]
    String content

}