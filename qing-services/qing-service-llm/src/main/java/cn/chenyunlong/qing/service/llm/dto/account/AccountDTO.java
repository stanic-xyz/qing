package cn.chenyunlong.qing.service.llm.dto.account;

import cn.chenyunlong.qing.service.llm.dto.channel.ChannelDto;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.enums.AccountStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.ProcessStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountDTO {

    private Long id;
    private String accountId;
    private String accountName;     // 必填
    private AccountType accountType;     // 必填 DEBIT/CREDIT/WALLET
    private String bankName;
    private String channel;         // channel code
    private ChannelDto channelDto;

    private String icon;
    private String remark;
    private String cardNumber;

    private LocalDateTime openingDate;
    private BigDecimal initialBalance;
    private BigDecimal currentBalance;
    private LocalDateTime balanceAsOf;
    private AccountStatusEnum status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AccountDTO of(Account account) {
        if (account == null) return null;
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setAccountId(account.getAccountId());
        accountDTO.setAccountName(account.getAccountName());
        accountDTO.setAccountType(account.getAccountType());
        accountDTO.setBankName(account.getBankName());
        if (account.getChannel() != null) {
            accountDTO.setChannel(account.getChannel().getCode());
            accountDTO.setChannelDto(ChannelDto.of(account.getChannel()));
        }
        accountDTO.setIcon(account.getIcon());
        accountDTO.setRemark(account.getRemark());
        accountDTO.setCardNumber(account.getCardNumber());
        accountDTO.setOpeningDate(account.getOpeningDate());
        accountDTO.setInitialBalance(account.getInitialBalance());
        accountDTO.setCurrentBalance(account.getCurrentBalance());
        accountDTO.setBalanceAsOf(account.getBalanceAsOf());
        accountDTO.setStatus(account.getStatus());
        accountDTO.setCreatedAt(account.getCreatedAt());
        accountDTO.setUpdatedAt(account.getUpdatedAt());
        return accountDTO;
    }
}
