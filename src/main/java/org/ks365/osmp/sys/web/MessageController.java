package org.ks365.osmp.sys.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ks365.osmp.common.ctrl.BaseController;
import org.ks365.osmp.common.entity.ResponseEntity;
import org.ks365.osmp.sys.entity.MessageEntity;
import org.ks365.osmp.sys.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息管理Controller
 *
 * @author tianslc
 */
@RestController
@RequestMapping(value = "/system/message")
public class MessageController extends BaseController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequiresPermissions("system:message:view")
    @PostMapping("list")
    public ResponseEntity<Page<MessageEntity>> list(@RequestBody MessageEntity messageEntity) {
        return new ResponseEntity<Page<MessageEntity>>().ok("获取成功").result(messageService.getPageByColumn(messageEntity));
    }

    @RequiresPermissions("system:message:view")
    @PostMapping("findList")
    public ResponseEntity<List<MessageEntity>> findList(@RequestBody MessageEntity messageEntity) {
        return new ResponseEntity<List<MessageEntity>>().ok("获取成功").result(messageService.getListByColumn(messageEntity));
    }

    @RequiresPermissions("system:message:edit")
    @PostMapping("save")
    public ResponseEntity<MessageEntity> save(@RequestBody MessageEntity messageEntity) {
        return new ResponseEntity<MessageEntity>().ok("保存成功").result(messageService.save(messageEntity));
    }

    @RequiresPermissions("system:message:edit")
    @DeleteMapping("delete/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        messageService.deleteById(id);
        return new ResponseEntity().ok("保存成功");
    }
}
