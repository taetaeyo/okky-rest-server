package com.okky.restserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.okky.restserver.domain.Banner;

public interface BannerRepository extends JpaRepository<Banner, Long>{

}
