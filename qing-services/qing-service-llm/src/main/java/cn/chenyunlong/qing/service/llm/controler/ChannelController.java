package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Channel;
import cn.chenyunlong.qing.service.llm.entity.ChannelAccountRel;
import cn.chenyunlong.qing.service.llm.service.ChannelAccountRelService;
import cn.chenyunlong.qing.service.llm.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;
    private final ChannelAccountRelService relService;

    @GetMapping
    public Result<List<Channel>> getAllChannels() {
        return Result.success(channelService.getAllChannels());
    }

    @GetMapping("/effective")
    public Result<List<Channel>> getEffectiveChannels() {
        return Result.success(channelService.getAllEffectiveChannels());
    }

    @PostMapping
    public Result<Channel> createChannel(@RequestBody Channel channel) {
        return Result.success(channelService.createOrUpdateChannel(channel));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteChannel(@PathVariable("id") Long id) {
        channelService.deleteChannel(id);
        return Result.success(null);
    }

    @PostMapping("/{id}/submit")
    public Result<Void> submitApproval(@PathVariable("id") Long id) {
        channelService.submitApproval(id);
        return Result.success(null);
    }

    @PostMapping("/{id}/approve")
    public Result<Void> approve(@PathVariable("id") Long id) {
        channelService.approve(id);
        return Result.success(null);
    }

    @PostMapping("/{id}/reject")
    public Result<Void> reject(@PathVariable("id") Long id) {
        channelService.reject(id);
        return Result.success(null);
    }

    @GetMapping("/{id}/accounts")
    public Result<List<Account>> getEffectiveAccounts(@PathVariable("id") Long id) {
        return Result.success(relService.getEffectiveAccountsByChannelId(id));
    }

    @PostMapping("/{channelId}/accounts/{accountId}/bind")
    public Result<ChannelAccountRel> bindAccount(@PathVariable("channelId") Long channelId, @PathVariable("accountId") Long accountId) {
        return Result.success(relService.bind(channelId, accountId));
    }

    @DeleteMapping("/{channelId}/accounts/{accountId}/unbind")
    public Result<Void> unbindAccount(@PathVariable("channelId") Long channelId, @PathVariable("accountId") Long accountId) {
        relService.unbind(channelId, accountId);
        return Result.success(null);
    }
}
