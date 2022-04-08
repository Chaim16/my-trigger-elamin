package me.zhengjie.modules.history.service.mapstruct;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import me.zhengjie.modules.history.domain.HistoryJob;
import me.zhengjie.modules.history.service.dto.HistoryJobDto;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-04-09T05:24:16+0800",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_301 (Oracle Corporation)"
)
@Component
public class HistoryJobMapperImpl implements HistoryJobMapper {

    @Override
    public HistoryJob toEntity(HistoryJobDto dto) {
        if ( dto == null ) {
            return null;
        }

        HistoryJob historyJob = new HistoryJob();

        historyJob.setId( dto.getId() );
        historyJob.setTriggerTime( dto.getTriggerTime() );
        historyJob.setCallName( dto.getCallName() );
        historyJob.setCallData( dto.getCallData() );
        historyJob.setCallType( dto.getCallType() );
        historyJob.setCallHost( dto.getCallHost() );
        historyJob.setCron( dto.getCron() );
        historyJob.setCreateTime( dto.getCreateTime() );
        historyJob.setModifyTime( dto.getModifyTime() );
        historyJob.setApp( dto.getApp() );
        historyJob.setCallerrorRetryCount( dto.getCallerrorRetryCount() );
        historyJob.setRunRetry( dto.getRunRetry() );

        return historyJob;
    }

    @Override
    public HistoryJobDto toDto(HistoryJob entity) {
        if ( entity == null ) {
            return null;
        }

        HistoryJobDto historyJobDto = new HistoryJobDto();

        historyJobDto.setId( entity.getId() );
        historyJobDto.setTriggerTime( entity.getTriggerTime() );
        historyJobDto.setCallName( entity.getCallName() );
        historyJobDto.setCallData( entity.getCallData() );
        historyJobDto.setCallType( entity.getCallType() );
        historyJobDto.setCallHost( entity.getCallHost() );
        historyJobDto.setCron( entity.getCron() );
        historyJobDto.setCreateTime( entity.getCreateTime() );
        historyJobDto.setModifyTime( entity.getModifyTime() );
        historyJobDto.setApp( entity.getApp() );
        historyJobDto.setCallerrorRetryCount( entity.getCallerrorRetryCount() );
        historyJobDto.setRunRetry( entity.getRunRetry() );

        return historyJobDto;
    }

    @Override
    public List<HistoryJob> toEntity(List<HistoryJobDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<HistoryJob> list = new ArrayList<HistoryJob>( dtoList.size() );
        for ( HistoryJobDto historyJobDto : dtoList ) {
            list.add( toEntity( historyJobDto ) );
        }

        return list;
    }

    @Override
    public List<HistoryJobDto> toDto(List<HistoryJob> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<HistoryJobDto> list = new ArrayList<HistoryJobDto>( entityList.size() );
        for ( HistoryJob historyJob : entityList ) {
            list.add( toDto( historyJob ) );
        }

        return list;
    }
}
