package me.zhengjie.modules.trigger.service.mapstruct;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import me.zhengjie.modules.trigger.domain.TriggerJob;
import me.zhengjie.modules.trigger.service.dto.TriggerJobDto;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-04-09T06:29:12+0800",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_301 (Oracle Corporation)"
)
@Component
public class TriggerJobMapperImpl implements TriggerJobMapper {

    @Override
    public TriggerJob toEntity(TriggerJobDto dto) {
        if ( dto == null ) {
            return null;
        }

        TriggerJob triggerJob = new TriggerJob();

        triggerJob.setId( dto.getId() );
        triggerJob.setStatus( dto.getStatus() );
        triggerJob.setTriggerTime( dto.getTriggerTime() );
        triggerJob.setRemove( dto.getRemove() );
        triggerJob.setCallName( dto.getCallName() );
        triggerJob.setCallData( dto.getCallData() );
        triggerJob.setCallType( dto.getCallType() );
        triggerJob.setCallHost( dto.getCallHost() );
        triggerJob.setCron( dto.getCron() );
        triggerJob.setCreateTime( dto.getCreateTime() );
        triggerJob.setModifyTime( dto.getModifyTime() );
        triggerJob.setApp( dto.getApp() );
        triggerJob.setCallerrorRetryCount( dto.getCallerrorRetryCount() );
        triggerJob.setRunRetry( dto.getRunRetry() );

        return triggerJob;
    }

    @Override
    public TriggerJobDto toDto(TriggerJob entity) {
        if ( entity == null ) {
            return null;
        }

        TriggerJobDto triggerJobDto = new TriggerJobDto();

        triggerJobDto.setId( entity.getId() );
        triggerJobDto.setStatus( entity.getStatus() );
        triggerJobDto.setTriggerTime( entity.getTriggerTime() );
        triggerJobDto.setRemove( entity.getRemove() );
        triggerJobDto.setCallName( entity.getCallName() );
        triggerJobDto.setCallData( entity.getCallData() );
        triggerJobDto.setCallType( entity.getCallType() );
        triggerJobDto.setCallHost( entity.getCallHost() );
        triggerJobDto.setCron( entity.getCron() );
        triggerJobDto.setCreateTime( entity.getCreateTime() );
        triggerJobDto.setModifyTime( entity.getModifyTime() );
        triggerJobDto.setApp( entity.getApp() );
        triggerJobDto.setCallerrorRetryCount( entity.getCallerrorRetryCount() );
        triggerJobDto.setRunRetry( entity.getRunRetry() );

        return triggerJobDto;
    }

    @Override
    public List<TriggerJob> toEntity(List<TriggerJobDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<TriggerJob> list = new ArrayList<TriggerJob>( dtoList.size() );
        for ( TriggerJobDto triggerJobDto : dtoList ) {
            list.add( toEntity( triggerJobDto ) );
        }

        return list;
    }

    @Override
    public List<TriggerJobDto> toDto(List<TriggerJob> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<TriggerJobDto> list = new ArrayList<TriggerJobDto>( entityList.size() );
        for ( TriggerJob triggerJob : entityList ) {
            list.add( toDto( triggerJob ) );
        }

        return list;
    }
}
