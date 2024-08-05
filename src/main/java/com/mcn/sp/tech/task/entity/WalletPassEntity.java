package com.mcn.sp.tech.task.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import de.brendamour.jpasskit.PKPass;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "wallet_pass")
@TypeDef(name = "json", typeClass = JsonType.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletPassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pass_type_id")
    private String passTypeId;

    @Column(name = "account_number")
    private String accountNumber;

    @Type(type = "json")
    @Column(name = "pass", columnDefinition = "json")
    private PKPass pass;

    @Column(name = "date")
    private LocalDate date;

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
