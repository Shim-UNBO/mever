package com.mever.api.domain.mainAdmin.repository;

import com.sun.tools.javac.Main;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MainMapper {
    List<Main> getMenuList();
}
