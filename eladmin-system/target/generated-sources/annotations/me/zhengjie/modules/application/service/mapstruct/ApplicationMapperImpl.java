package me.zhengjie.modules.application.service.mapstruct;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import me.zhengjie.modules.application.domain.Application;
import me.zhengjie.modules.application.service.dto.ApplicationDto;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-04-07T23:33:26+0800",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_301 (Oracle Corporation)"
)
@Component
public class ApplicationMapperImpl implements ApplicationMapper {

    @Override
    public Application toEntity(ApplicationDto dto) {
        if ( dto == null ) {
            return null;
        }

        Application application = new Application();

        application.setId( dto.getId() );
        application.setName( dto.getName() );
        application.setCreateTime( dto.getCreateTime() );
        application.setModifyTime( dto.getModifyTime() );

        return application;
    }

    @Override
    public ApplicationDto toDto(Application entity) {
        if ( entity == null ) {
            return null;
        }

        ApplicationDto applicationDto = new ApplicationDto();

        applicationDto.setId( entity.getId() );
        applicationDto.setName( entity.getName() );
        applicationDto.setCreateTime( entity.getCreateTime() );
        applicationDto.setModifyTime( entity.getModifyTime() );

        return applicationDto;
    }

    @Override
    public List<Application> toEntity(List<ApplicationDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Application> list = new ArrayList<Application>( dtoList.size() );
        for ( ApplicationDto applicationDto : dtoList ) {
            list.add( toEntity( applicationDto ) );
        }

        return list;
    }

    @Override
    public List<ApplicationDto> toDto(List<Application> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ApplicationDto> list = new ArrayList<ApplicationDto>( entityList.size() );
        for ( Application application : entityList ) {
            list.add( toDto( application ) );
        }

        return list;
    }
}
