package org.rostfactory.bank.controller;

import lombok.RequiredArgsConstructor;
import org.rostfactory.bank.dto.AccountBankDtoRequest;
import org.rostfactory.bank.dto.AccountBankDtoRequestCreate;
import org.rostfactory.bank.dto.AccountBankDtoResponse;
import org.rostfactory.bank.entity.AccountBank;
import org.rostfactory.bank.mapper.BankMapper;
import org.rostfactory.bank.service.BankService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bank")
public class BankController {

    private final BankService service;
    private final BankMapper mapper;

    @GetMapping("/findAll")
    public List<AccountBankDtoResponse> findAllAccount() {
        return service.findAllAccount().stream()
                .map(mapper::toDto)
                .toList();
    }

    @GetMapping("/find/{id}")
    public AccountBankDtoResponse findAccountById(@PathVariable long id) {
        return mapper.toDto(service.findById(id));
    }

    @GetMapping("/findAccountBankByCitizenId/{id}")
    public AccountBankDtoResponse findAccountBankByCitizenId(@PathVariable long id) {
        AccountBank accountBankEntity = service.findByCitizenId(id);
        return mapper.toDto(accountBankEntity);
    }

    @PostMapping("/create")
    public AccountBankDtoResponse createAccountBank(@RequestBody AccountBankDtoRequestCreate account) {
        AccountBank accountBankEntity = mapper.toEntityForCreate(account);
        return mapper.toDto(service.create(accountBankEntity));
    }

    @PatchMapping("/update")
    public AccountBankDtoResponse updateAccountBank(@RequestBody AccountBankDtoRequest account) {
        AccountBank accountBankEntity = mapper.toEntity(account);
        return mapper.toDto(service.update(accountBankEntity));
    }

    @DeleteMapping("/deleteByAccountBankId/{id}")
    public void deleteByAccountBankId(@PathVariable long id) {
        service.deleteByAccountBankId(id);
    }

    @DeleteMapping("/deleteByCitizenId/{id}")
    public void deleteByCitizenId(@PathVariable long id) {
        service.deleteByCitizenId(id);
    }

    @GetMapping("/checkMoneyFromCitizen/{citizenId}")
    public Boolean isEnoughMoneyForPurchaseByCitizenId(@PathVariable long citizenId, @RequestParam int purchasePrice) {
        return service.isEnoughMoneyForPurchaseByCitizenId(citizenId, purchasePrice);
    }
}
