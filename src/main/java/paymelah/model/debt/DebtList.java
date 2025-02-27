package paymelah.model.debt;

import static java.util.Objects.requireNonNull;
import static paymelah.commons.util.CollectionUtil.requireAllNonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import paymelah.model.debt.exceptions.DebtNotFoundException;

/**
 * Represents a list of a person's {@link Debt} in the address book.
 * Guarantees: debts are null, field values are validated, immutable.
 */
public class DebtList {
    private final List<Debt> debts = new ArrayList<>();
    private BigDecimal totalDebt = new BigDecimal(0).setScale(2);

    /**
     * Constructs a {@code DebtList}.
     * {@code debts} must be present and all debts must be not null.
     *
     * @param debtList The list of debts owed.
     */
    public DebtList(DebtList debtList) {
        requireAllNonNull(debtList);
        debts.addAll(debtList.debts);
        totalDebt = debtList.totalDebt;
    }

    /**
     * Constructs an empty {@code DebtList}.
     */
    public DebtList() {}

    /**
     * Returns true if the list contains an equivalent debt as the given argument.
     *
     * @param toCheck The debt to check for in the list.
     * @return true if the list contains an equivalent debt as the given argument.
     */
    public boolean contains(Debt toCheck) {
        requireNonNull(toCheck);
        return debts.stream().anyMatch(toCheck::equals);
    }

    public BigDecimal getTotalDebt() {
        return totalDebt;
    }

    /**
     * Adds a debt to the list.
     *
     * @param toAdd The debt to add to the list.
     * @return The modified debt list.
     */
    public DebtList addDebt(Debt toAdd) {
        requireNonNull(toAdd);
        DebtList edited = new DebtList(this);
        edited.debts.add(toAdd);
        edited.totalDebt = totalDebt.add(toAdd.getMoney().getValue());
        return edited;
    }

    /**
     * Removes a debt from the list.
     *
     * @param toRemove The debt to remove from the list.
     * @return The modified debt list.
     */
    public DebtList removeDebt(Debt toRemove) {
        requireNonNull(toRemove);
        if (!contains(toRemove)) {
            throw new DebtNotFoundException();
        }
        DebtList edited = new DebtList(this);
        edited.debts.remove(toRemove);
        edited.totalDebt = totalDebt.subtract(toRemove.getMoney().getValue());
        return edited;
    }

    /**
     * Returns an immutable debt list, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     *
     * @return An immutable debt list.
     */
    public List<Debt> asList() {
        return Collections.unmodifiableList(debts);
    }

    /**
     * Returns a debt list with the elements from {@code list}.
     *
     * @param list The list of {@code Debt}s to use.
     * @return The new DebtList.
     */
    public static DebtList fromList(List<Debt> list) {
        DebtList debtList = new DebtList();
        for (Debt debt : list) {
            debtList.debts.add(debt);
            debtList.totalDebt = debtList.totalDebt.add(debt.getMoney().getValue());
        }
        return debtList;
    }

    /**
     * Returns whether this DebtList is empty.
     *
     * @return true only if the DebtList is empty.
     */
    public boolean isEmpty() {
        return debts.isEmpty();
    }

    /**
     * Returns true only if both debts have the same data fields.
     *
     * @param other The other {@code Debt} object to check.
     * @return true only if both debts have the same data fields.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DebtList)) {
            return false;
        }

        DebtList otherDebt = (DebtList) other;
        return otherDebt.asList().equals(asList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(debts);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        int i = 1;
        for (Debt debt : debts) {
            builder.append(i + ". ")
                    .append(debt.toString())
                    .append("\n");
            i++;
        }
        return builder.toString().equals("") ? "No debts" : builder.toString();
    }
}
