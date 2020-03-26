import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class BMICalculatorTest
{
    private String enviroment = "prod";

    @BeforeAll
    static void beforeAll()
     {
         System.out.println("Before alle unit tests");
     }

     @AfterAll
     static void afterAll()
     {
         System.out.println("After all unit tests");
     }

     @Nested
     class IsDietRecommendedTests
     {
         @Test
         void shouldReturnTrueWhenDietRecommended()
         {
             // given
             double weight = 83.0;
             double height = 1.72;

             // when
             boolean recommended = BMICalculator.isDietRecommended(weight,height);

             // then
             assertTrue(recommended);
         }
         @Test
         void shouldReturnTrueWhenDietRecommendedv2()
         {
             // given
             double weight = 84.0;
             double height = 1.72;

             // when
             boolean recommended = BMICalculator.isDietRecommended(weight,height);

             // then
             assertTrue(recommended);
         }

         @ParameterizedTest
         @ValueSource(doubles = {89.0, 95.0, 110.0})
         void shouldReturnTrueWhenDietRecommendedV2(Double coderWeight)
         {
             // given
             double weight = coderWeight;
             double height = 1.72;

             // when
             boolean recommended = BMICalculator.isDietRecommended(weight,height);

             // then
             assertTrue(recommended);
         }

         @ParameterizedTest(name = "weight={0}, height={1}")
         @CsvSource(value = {"89.0, 1.72", "95.0, 1.75", "110.0, 1.78"})
         void shouldReturnTrueWhenDietRecommendedV3(Double coderWeight, Double coderHeight)
         {
             // given
             double weight = coderWeight;
             double height = coderHeight;

             // when
             boolean recommended = BMICalculator.isDietRecommended(weight,height);

             // then
             assertTrue(recommended);
         }

         @ParameterizedTest(name = "weight={0}, height={1}")
         @CsvFileSource(resources = "/original.csv", numLinesToSkip = 1)
         void shouldReturnTrueWhenDietRecommendedV4(Double coderWeight, Double coderHeight)
         {
             // given
             double weight = coderWeight;
             double height = coderHeight;

             // when
             boolean recommended = BMICalculator.isDietRecommended(weight,height);

             // then
             assertTrue(recommended);
         }

         @Test
         void shouldReturnfalseWhenDietNotRecommended()
         {
             // given
             double weight = 50.0;
             double height = 1.92;

             // when
             boolean recommended = BMICalculator.isDietRecommended(weight,height);

             // then
             assertFalse(recommended);
         }
         @Test
         void shouldThrowArithmeticExceptionWhenHeightZero()
         {
             // given
             double weight = 1.0;
             double height = 0.0;

             // when
             Executable executable = () -> BMICalculator.isDietRecommended(weight,height);

             // then
             assertThrows(ArithmeticException.class, executable);
         }
     }

     @Nested
     class FindCodersWithWorstBMITests
     {
         @Test
         void shouldReturnCodeWithWorstBMIWhenCoderListNotEmpty()
         {
             // given
             List<Coder> coders = new ArrayList<>();
             coders.add(new Coder(1.80,60.0));
             coders.add(new Coder(1.82,98.0));
             coders.add(new Coder(1.82,64.7));

             // when
             Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

             // then
             assertAll(
                     () -> Assertions.assertEquals(1.82, coderWorstBMI.getHeight()),
                     () -> Assertions.assertEquals(98.0, coderWorstBMI.getWeight())
             );
         }

         @Test
         void shouldReturnNullWorstBMICoderWhenCoderListEmpty()
         {
             // given
             List<Coder> coders = new ArrayList<>();

             // when
             Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

             // then
             assertNull(coderWorstBMI);
         }

         @Test
         void shouldReturnCoderWithWorstBMIInMsWhenCoderListHas10000Elements()
         {
             // given
             assumeTrue(BMICalculatorTest.this.enviroment.equals("prod"));
             List<Coder> coders = new ArrayList<>();
             for (int i = 0; i < 10000; i++) {
                 coders.add(new Coder(1.0 + i, 10.0 + i));
             }

             // when
             Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);

             // then
             assertTimeout(Duration.ofMillis(500), executable);
         }
     }

     @Nested
     class GetBMIScoresTests
     {
         @Test
         void shouldReturnCorrectBMIScoreArrayWhenCoderListNotEmpty()
         {
             // given
             List<Coder> coders = new ArrayList<>();
             coders.add(new Coder(1.80,60.0));
             coders.add(new Coder(1.82,98.0));
             coders.add(new Coder(1.82,64.7));
             double[] expected = {18.52, 29.59, 19.53};

             // when
             double[] bmiScores = BMICalculator.getBMIScores(coders);

             // then
             assertArrayEquals(expected, bmiScores);
         }
     }
}