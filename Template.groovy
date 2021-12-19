// 依赖
@Grab("com.alibaba:fastjson:1.2.9")

// 导入包
import com.alibaba.fastjson.JSON
import java.math.BigDecimal
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TreeMap
import org.springframework.util.StringUtils

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

    // 创建订单
    def create(GroovyExecuteScriptInputDTO input) {

        // 在此处编写创建订单实现
        GroovySignUtil groovySignUtil = new GroovySignUtil()
        GroovyHttpUtil groovyHttpUtil = new GroovyHttpUtil()
        def bizContent = JSON.parseObject(input.getBizContent())
        Map<String, Object> treeMap = new TreeMap<>(String::compareTo)
        treeMap.put("MchId", bizContent.channelMerchnatId)
        treeMap.put("MchOrderNo", input.getCalleeOrderId())
        treeMap.put("NotifyUrl", bizContent.callbackUrl)
        treeMap.put("RequestTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(input.getOrderTime()))
        treeMap.put("CategoryCode", bizContent.channelCode)
        treeMap.put("Amount", input.getAmount())
        treeMap.put("Attach", "这是附加信息")
        treeMap.put("Title", "这是一笔订单")
        treeMap.put("Sign", groovySignUtil.doSign(treeMap, bizContent.channelMerchnatKey))

        def res = groovyHttpUtil.doPost(bizContent.createOrderUrl, JSON.toJSONString(treeMap))
        def resJson = JSON.parseObject(res)

        GroovyExecuteScriptOutputDTO output = new GroovyExecuteScriptOutputDTO()
        if (!resJson.Success) {
            output.setOrderStatus("AF")
            return JSON.toJSONString(output)
        }
        output.setCodeMemberId(bizContent.channelMerchnatId)
        output.setOrderId(input.getOrderId())
        output.setCalleeOrderId(input.getCalleeOrderId())
        output.setOrderStatus("NP")
        output.setAmount(input.getAmount())
        output.setActualAmount(input.getAmount())
        output.setChannelReturnMessage(JSON.toJSONString(resJson.ResData))
        output.setContent(res)
        return JSON.toJSONString(output)
    }

    // 调单查询
    def query(GroovyExecuteScriptInputDTO input) {

        // 在此处编写调单查询实现

        GroovyExecuteScriptOutputDTO output = new GroovyExecuteScriptOutputDTO(orderId: input.getOrderId(), calleeOrderId: input.getCalleeOrderId(), orderStatus: "AP", amount: input.getAmount(), actualAmount: new BigDecimal("100"))

        return JSON.toJSONString(output)
    }

    // 渠道回调解析请求参数
    def parse(GroovyExecuteScriptInputDTO input) {

        // 在此处编写渠道回调解析请求参数实现

        GroovyExecuteScriptOutputDTO output = new GroovyExecuteScriptOutputDTO(orderId: "订单编号", calleeOrderId: "被调用方订单编号", orderStatus: "AP", amount: new BigDecimal("100"), actualAmount: new BigDecimal("100"))

        return JSON.toJSONString(output)
    }

}

// Http工具
class GroovyHttpUtil {

    // Post请求
    def doPost(url, data) {
        def conn = new URL(url).openConnection()
        conn.setRequestMethod("POST")
        conn.setRequestProperty("Content-Type", "application/json")
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

        def result = ""

        for (String item : map.keySet()) {

            if (StringUtils.isEmpty(result)) {
                result = item.toLowerCase() + "=" + map.get(item)
                continue
            }

            result = result + "&" + item.toLowerCase() + "=" + map.get(item)
        }

        result = result + "&key=" + key

        return MessageDigest.getInstance("MD5").digest("$result".bytes).encodeHex().toString().toLowerCase()
    }

}

// 输入对象
class GroovyExecuteScriptInputDTO {
    String scriptMethod         // 脚本方法
    String memberId             // 会员编号
    String orderId              // 订单编号
    String calleeOrderId        // 被调用方订单编号
    BigDecimal amount           // 订单金额
    Date orderTime              // 订单时间
    String implPath             // 实现路径
    String bizContent
    // 业务大字段[{"channelMerchnatId":"商户编号","channelMerchnatKey":"商户密钥","channelCode":"渠道编号","subChannelCode":"子渠道编号","callbackUrl":"回调地址","redirectUrl":"重定向地址","createOrderUrl":"创建订单地址","queryOrderUrl":"调单查询地址"}]
    String args                 // 参数[{"parameter":{},"header":{},"body":{}}]
    String clientIp             // 商户客户端IP
    String redirectUrl          // 商户重定向地址
}

// 输出对象
class GroovyExecuteScriptOutputDTO {
    String codeMemberId         // 码商会员编号[自研渠道必须返回]
    String orderId              // 订单编号
    String calleeOrderId        // 被调用方订单编号
    String orderStatus          // 订单状态
    BigDecimal amount           // 订单金额
    BigDecimal actualAmount     // 实际金额
    String channelReturnMessage // 网关渠道返回信息
    String content              // 报文[作用一:商户下单后返回商户({"text":"html代码","url":"url地址"});作用二:渠道回调时返回渠道(不同渠道的成功标识)]
}