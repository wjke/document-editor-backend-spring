package com.natj.documents.repositories;

import com.natj.documents.models.TemplateField;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TemplateFieldRepository extends CrudRepository<TemplateField, Long> {
    List<TemplateField> findByTitle(String title);

    @Query("SELECT MAX(ordering) FROM #{#entityName} WHERE template_id = :templateId")
    Integer findMaxOrdering(@Param("templateId") long templateId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE #{#entityName} SET ordering = ordering - 1 WHERE template_id = :templateId AND ordering > :ordering")
    void updateOrderingAfterDelete(@Param("templateId") long templateId, @Param("ordering") int ordering);

    /**
     * Первая функция для замены(swap) полей сортировки(ordering), ЗАПУСКАТЬ ПЕРВОЙ.
     * Заменяет сортировку(ordering) на отрицательную. т.е. было 2 и надо увеличить сортировку, тогда у элемента с 2
     * станет -3, а у элемента(если увеличиваем, то это будет следующий элемент) с 3 будет -2
     */
    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} " +
            "SET ordering = " +
            "CASE " +
            "   WHEN ordering = :ordering THEN -(:ordering + :upOrDown) " +
            "   WHEN ordering = :ordering + :upOrDown THEN -:ordering " +
            "END " +
            "WHERE ordering IN (:ordering, :ordering + :upOrDown) AND template_id = :templateId")
    void upOrDownOrderingStart(@Param("templateId") long templateId, @Param("ordering") int ordering, @Param("upOrDown") int upOrDown);

    /**
     * Вторая функция для замены(swap) полей сортировки(ordering), ЗАПУСКАТЬ ВТОРОЙ.
     * Ищет отрицаетльные значения и меняет на положительные.
     * clearAutomatically необходим для того, чтобы после функции сделать flush
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE #{#entityName} SET ordering = -ordering WHERE ordering IN (-:ordering, -(:ordering + :upOrDown)) AND template_id = :templateId")
    void upOrDownOrderingEnd(@Param("templateId") long templateId, @Param("ordering") int ordering, @Param("upOrDown") int upOrDown);
}
