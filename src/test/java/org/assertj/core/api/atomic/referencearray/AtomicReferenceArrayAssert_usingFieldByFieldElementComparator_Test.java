/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.atomic.referencearray;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_STRING;
import static org.assertj.core.test.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReferenceArray;

import org.assertj.core.api.AtomicReferenceArrayAssert;
import org.assertj.core.api.AtomicReferenceArrayAssertBaseTest;
import org.assertj.core.internal.AtomicReferenceArrayElementComparisonStrategy;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.test.Jedi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AtomicReferenceArrayAssert_usingFieldByFieldElementComparator_Test
    extends AtomicReferenceArrayAssertBaseTest {

  private ObjectArrays arraysBefore;

  @BeforeEach
  void before() {
    arraysBefore = getArrays(assertions);
  }

  @Override
  protected AtomicReferenceArrayAssert<Object> invoke_api_method() {
    return assertions.usingFieldByFieldElementComparator();
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(arraysBefore).isNotSameAs(getArrays(assertions));
    assertThat(getArrays(assertions).getComparisonStrategy()).isInstanceOf(ComparatorBasedComparisonStrategy.class);
    assertThat(getObjects(assertions).getComparisonStrategy()).isInstanceOf(AtomicReferenceArrayElementComparisonStrategy.class);
  }

  @Test
  void successful_isEqualTo_assertion_using_field_by_field_element_comparator() {
    AtomicReferenceArray<Foo> array1 = atomicArrayOf(new Foo("id", 1));
    Foo[] array2 = array(new Foo("id", 1));
    assertThat(array1).usingFieldByFieldElementComparator().isEqualTo(array2);
  }

  @Test
  void successful_isIn_assertion_using_field_by_field_element_comparator() {
    AtomicReferenceArray<Foo> array1 = atomicArrayOf(new Foo("id", 1));
    Foo[] array2 = array(new Foo("id", 1));
    assertThat(array1).usingFieldByFieldElementComparator().isIn(array2, array2);
  }

  @Test
  void successful_isEqualTo_assertion_using_field_by_field_element_comparator_with_heterogeneous_array() {
    AtomicReferenceArray<Animal> array1 = new AtomicReferenceArray<>(array(new Bird("White"), new Snake(15)));
    Animal[] array2 = array(new Bird("White"), new Snake(15));
    assertThat(array1).usingFieldByFieldElementComparator().isEqualTo(array2);
  }

  @Test
  void successful_contains_assertion_using_field_by_field_element_comparator_with_heterogeneous_array() {
    AtomicReferenceArray<Animal> array1 = new AtomicReferenceArray<>(array(new Bird("White"), new Snake(15)));
    assertThat(array1).usingFieldByFieldElementComparator()
                      .contains(new Snake(15), new Bird("White"))
                      .contains(new Bird("White"), new Snake(15));
    assertThat(array1).usingFieldByFieldElementComparator()
                      .containsOnly(new Snake(15), new Bird("White"))
                      .containsOnly(new Bird("White"), new Snake(15));
  }

  @Test
  void successful_isIn_assertion_using_field_by_field_element_comparator_with_heterogeneous_array() {
    AtomicReferenceArray<Animal> array1 = new AtomicReferenceArray<>(array(new Bird("White"), new Snake(15)));
    Animal[] array2 = array(new Bird("White"), new Snake(15));
    assertThat(array1).usingFieldByFieldElementComparator().isIn(array2, array2);
  }

  @Test
  void successful_containsExactly_assertion_using_field_by_field_element_comparator_with_heterogeneous_array() {
    Animal[] array = array(new Bird("White"), new Snake(15));
    AtomicReferenceArray<Animal> array1 = new AtomicReferenceArray<>(array);
    assertThat(array1).usingFieldByFieldElementComparator().containsExactly(new Bird("White"), new Snake(15));
  }

  @Test
  void successful_containsExactlyInAnyOrder_assertion_using_field_by_field_element_comparator_with_heterogeneous_array() {
    Snake snake = new Snake(15);
    AtomicReferenceArray<Animal> array1 = new AtomicReferenceArray<>(array(new Bird("White"), snake, snake));
    assertThat(array1).usingFieldByFieldElementComparator().containsExactlyInAnyOrder(new Snake(15), new Bird("White"),
                                                                                      new Snake(15));
  }

  @Test
  void successful_containsExactlyInAnyOrderElementsOf_assertion_using_field_by_field_element_comparator_with_heterogeneous_array() {
    Snake snake = new Snake(15);
    AtomicReferenceArray<Animal> array1 = new AtomicReferenceArray<>(array(new Bird("White"), snake, snake));
    assertThat(array1).usingFieldByFieldElementComparator().containsExactlyInAnyOrderElementsOf(
                                                                                                newArrayList(new Snake(15),
                                                                                                             new Bird("White"),
                                                                                                             new Snake(15)));
  }

  @Test
  void successful_containsOnly_assertion_using_field_by_field_element_comparator_with_unordered_array() {
    Person goodObiwan = new Person("Obi-Wan", "Kenobi", "good man");
    Person badObiwan = new Person("Obi-Wan", "Kenobi", "bad man");

    Person[] list = array(goodObiwan, badObiwan);
    assertThat(list).usingFieldByFieldElementComparator().containsOnly(badObiwan, goodObiwan);
  }

  private class Person {
    private String first, last, info;

    public Person(String first, String last, String info) {
      this.first = first;
      this.last = last;
      this.info = info;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Person person = (Person) o;
      return Objects.equals(first, person.first) && Objects.equals(last, person.last);
    }

    @Override
    public int hashCode() {
      return Objects.hash(first, last);
    }

    @Override
    public String toString() {
      return String.format("Person{first='%s', last='%s', info='%s'}",
                           first, last, info);
    }
  }

  @Test
  void failed_isEqualTo_assertion_using_field_by_field_element_comparator() {
    // GIVEN
    Foo[] array1 = array(new Foo("id", 1));
    Foo[] array2 = array(new Foo("id", 2));

    // WHEN
    Throwable error = catchThrowable(() -> assertThat(array1).usingFieldByFieldElementComparator().isEqualTo(array2));

    // THEN
    assertThat(error).isInstanceOf(AssertionError.class)
                     .hasMessage(format(shouldBeEqualMessage("[Foo(id=id, bar=1)]", "[Foo(id=id, bar=2)]") + "%n"
                                        + "when comparing elements using field/property by field/property comparator on all fields/properties%n"
                                        + "Comparators used:%n"
                                        + "- for elements fields (by type): {Double -> DoubleComparator[precision=1.0E-15], Float -> FloatComparator[precision=1.0E-6], Path -> lexicographic comparator (Path natural order)}%n"
                                        + "- for elements (by type): {Double -> DoubleComparator[precision=1.0E-15], Float -> FloatComparator[precision=1.0E-6], Path -> lexicographic comparator (Path natural order)}"));
  }

  @Test
  void failed_isIn_assertion_using_field_by_field_element_comparator() {
    // GIVEN
    AtomicReferenceArray<Foo> array1 = atomicArrayOf(new Foo("id", 1));
    Foo[] array2 = array(new Foo("id", 2));

    // WHEN
    Throwable error = catchThrowable(() -> assertThat(array1).usingFieldByFieldElementComparator().isIn(array2, array2));

    // THEN
    assertThat(error).isInstanceOf(AssertionError.class)
                     .hasMessage(format("%nExpecting actual:%n"
                                        + "  [Foo(id=id, bar=1)]%n"
                                        + "to be in:%n"
                                        + "  [[Foo(id=id, bar=2)], [Foo(id=id, bar=2)]]%n"
                                        + "when comparing elements using field/property by field/property comparator on all fields/properties%n"
                                        + "Comparators used:%n"
                                        + "- for elements fields (by type): {Double -> DoubleComparator[precision=1.0E-15], Float -> FloatComparator[precision=1.0E-6], Path -> lexicographic comparator (Path natural order)}%n"
                                        + "- for elements (by type): {Double -> DoubleComparator[precision=1.0E-15], Float -> FloatComparator[precision=1.0E-6], Path -> lexicographic comparator (Path natural order)}"));
  }

  @Test
  void should_be_able_to_use_a_comparator_for_specified_fields_of_elements_when_using_field_by_field_element_comparator() {
    Jedi actual = new Jedi("Yoda", "green");
    Jedi other = new Jedi("Luke", "green");

    assertThat(atomicArrayOf(actual)).usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING, "name")
                                     .usingFieldByFieldElementComparator()
                                     .contains(other);
  }

  @Test
  void comparators_for_element_field_names_should_have_precedence_over_comparators_for_element_field_types_when_using_field_by_field_element_comparator() {
    Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);
    Jedi actual = new Jedi("Yoda", "green");
    Jedi other = new Jedi("Luke", "green");

    assertThat(atomicArrayOf(actual)).usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING, "name")
                                     .usingComparatorForElementFieldsWithType(comparator, String.class)
                                     .usingFieldByFieldElementComparator()
                                     .contains(other);
  }

  @Test
  void should_be_able_to_use_a_comparator_for_element_fields_with_specified_type_when_using_field_by_field_element_comparator() {
    Jedi actual = new Jedi("Yoda", "green");
    Jedi other = new Jedi("Luke", "blue");

    assertThat(atomicArrayOf(actual)).usingComparatorForElementFieldsWithType(ALWAY_EQUALS_STRING, String.class)
                                     .usingFieldByFieldElementComparator()
                                     .contains(other);
  }

  public static class Foo {
    public final String id;
    public final int bar;

    public Foo(final String id, final int bar) {
      this.id = id;
      this.bar = bar;
    }

    @Override
    public String toString() {
      return "Foo(id=" + id + ", bar=" + bar + ")";
    }

  }

  private static class Animal {
    private final String name;

    private Animal(String name) {
      this.name = name;
    }

    @SuppressWarnings("unused")
    public String getName() {
      return name;
    }
  }

  private static class Bird extends Animal {
    private final String color;

    private Bird(String color) {
      super("Bird");
      this.color = color;
    }

    @SuppressWarnings("unused")
    public String getColor() {
      return color;
    }
  }

  private static class Snake extends Animal {
    private final int length;

    private Snake(int length) {
      super("Snake");
      this.length = length;
    }

    @SuppressWarnings("unused")
    public int getLength() {
      return length;
    }
  }
}
