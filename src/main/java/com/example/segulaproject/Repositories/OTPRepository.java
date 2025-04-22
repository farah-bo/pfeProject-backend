package com.example.segulaproject.Repositories;

import com.example.segulaproject.DTO.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository

public interface OTPRepository extends JpaRepository<OTP, Long> {

    OTP findByIdentificationAndExpiredDateAfter(String identification, Date now);

    OTP findByIdentification(String identification);
}