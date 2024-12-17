package sports.demoapi.controller.mapper;

import java.util.List;

public interface Mapper<O, D, R> {

    R convertToResponseDto(O entity);
    O convertToEntity(D dto);
    List<R> convertAllToResponseDto(List<O> entities);
}
