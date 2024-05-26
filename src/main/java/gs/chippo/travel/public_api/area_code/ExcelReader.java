//package gs.chippo.travel.public_api.area_code;
//
//import gs.chippo.travel.board.BoardEntity;
//import lombok.extern.log4j.Log4j2;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Component;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//
//// CommandLineRunner 는 spring boot가 시작되기전 초기 데이터를 넣기 위해 실행됨
//// 위 이유 때문에, AreaCode 테이블이 없을 시 에러가 남
//// 이거 전체 주석 처리하고 실행한번하고 다시 주석풀고 실행하면 데이터 들어감.
//// 더미데이터 넣기 위한 함수
//@Log4j2
//@Component
//public class ExcelReader implements CommandLineRunner {
//    @Autowired
//    private AreaCodeRepository areaCoderepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        String excelFilePath = "행정동코드_data.xlsx";
//        InputStream inputStream = new ClassPathResource(excelFilePath).getInputStream();
//        log.info(inputStream);
//        List<AreaCodeEntity> seoulAreas = readExcelFile(inputStream);
//        areaCoderepository.saveAll(seoulAreas);
//    }
//
//    private List<AreaCodeEntity> readExcelFile(InputStream inputStream) throws IOException {
//        List<AreaCodeEntity> areas = new ArrayList<>();
//        Workbook workbook = new XSSFWorkbook(inputStream);
//
//        Sheet sheet = workbook.getSheet("1. 총괄표(현행)");  // 시트 이름으로 가져오기
//        if (sheet == null) {
//            throw new IllegalArgumentException("시트를 찾을 수 없습니다: 1. 총괄표");
//        }
//        log.info(sheet.getSheetName());
//        log.info(sheet);
//        for (Row row : sheet) {
//            Cell cityCell = row.getCell(2);
//            if (cityCell != null && CellType.STRING.equals(cityCell.getCellType()) && "서울특별시".equals(cityCell.getStringCellValue()) && row.getCell(4) != null) {
//                AreaCodeEntity area = AreaCodeEntity.builder()
//                        .city(getCellValueAsString(row.getCell(2)))
//                        .district(getCellValueAsString(row.getCell(4)))
//                        .districtCode(getCellValueAsString(row.getCell(3)))
//                        .region(getCellValueAsString(row.getCell(6)))
//                        .regionCode(getCellValueAsString(row.getCell(5)))
//                        .englishName(getCellValueAsString(row.getCell(7)))
//                        .build();
//                areas.add(area);
//            }
//        }
//        workbook.close();
//        inputStream.close();
//        return areas;
//    }
//
//    private String getCellValueAsString(Cell cell) {
//        if (cell == null) {
//            return "";
//        }
//        switch (cell.getCellType()) {
//            case STRING:
//                return cell.getStringCellValue();
//            case NUMERIC:
//                if (DateUtil.isCellDateFormatted(cell)) {
//                    return cell.getDateCellValue().toString();
//                } else {
//                    return String.valueOf((long)cell.getNumericCellValue()); // 숫자형을 문자열로 변환
//                }
//            case BOOLEAN:
//                return String.valueOf(cell.getBooleanCellValue());
//            case FORMULA:
//                return cell.getCellFormula();
//            case BLANK:
//                return "";
//            default:
//                return "";
//        }
//    }
//}
