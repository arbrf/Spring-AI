package com.spring.ai.Spring.AI.Project.outputparser;

import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("parse")
public class ParserController {
    private final PracticeParser practiceParser;

    public ParserController(PracticeParser practiceParser) {
        this.practiceParser = practiceParser;
    }
    @GetMapping("/")
    public Pair getBeanOutPutConverter(){
        BeanOutputConverter<Pair> converter = new BeanOutputConverter<>(Pair.class);
        Pair pair = converter.convert(practiceParser.returnRawJson());
        System.out.println(pair.countryName());
        System.out.println(pair.cities());
        return pair;
    }

    @GetMapping("/map-converter")
    public Map<String ,Object> getMapOutPutConverter(){
        MapOutputConverter mapOutputConverter=new MapOutputConverter();
        Map<String, Object> convert = mapOutputConverter.convert(practiceParser.returnMapValues());

        System.out.println("mapoutputConverter ="+mapOutputConverter);
        System.out.println("convert ="+convert);
        System.out.println("mapoutput Converter ="+mapOutputConverter);
        String format = mapOutputConverter.getFormat();
        return convert;
    }

    @GetMapping("/list-converter")
    public List<String> getListOutPutConverter(){
        ListOutputConverter listOutputConverter=new ListOutputConverter(new DefaultConversionService());
        List<String> convert = listOutputConverter.convert(practiceParser.returnListOfValues());

        System.out.println("listOutputConverter ="+listOutputConverter);
        System.out.println("convert ="+convert);

        String format = listOutputConverter.getFormat();
        return convert;
    }
}
