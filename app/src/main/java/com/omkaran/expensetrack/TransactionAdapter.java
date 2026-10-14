package com.omkaran.expensetrack;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omkaran.expensetrack.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_TRANSACTION = 1;

    private List<Object> itemList;

    public TransactionAdapter(List<Object> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {

        if (itemList.get(position) instanceof String) {
            return TYPE_HEADER;
        }

        return TYPE_TRANSACTION;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        if (viewType == TYPE_HEADER) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(
                            R.layout.item_date_header,
                            parent,
                            false
                    );

            return new DateHeaderViewHolder(view);

        } else {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(
                            R.layout.item_transaction,
                            parent,
                            false
                    );

            return new TransactionViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder,
            int position) {

        Object item = itemList.get(position);

        // =========================
        // DATE HEADER
        // =========================

        if (holder instanceof DateHeaderViewHolder) {

            String header = (String) item;

            DateHeaderViewHolder headerHolder =
                    (DateHeaderViewHolder) holder;

            headerHolder.tvDateHeader.setText(header);

            return;
        }

        // =========================
        // TRANSACTION
        // =========================

        Transaction transaction =
                (Transaction) item;

        TransactionViewHolder transactionHolder =
                (TransactionViewHolder) holder;

        // -------------------------
        // Category
        // -------------------------

        String category =
                transaction.getCategory();

        if (category == null ||
                category.trim().isEmpty()) {

            category = "Other";
        }

        transactionHolder.tvCategory.setText(category);

        // -------------------------
        // Icon
        // -------------------------

        switch (category.toLowerCase()) {

            case "food":
                transactionHolder.tvIcon.setText("🍴");
                break;

            case "travel":
                transactionHolder.tvIcon.setText("✈️");
                break;

            case "shopping":
                transactionHolder.tvIcon.setText("🛒");
                break;

            case "business":
                transactionHolder.tvIcon.setText("💼");
                break;

            case "salary":
                transactionHolder.tvIcon.setText("💰");
                break;

            case "bills":
                transactionHolder.tvIcon.setText("🧾");
                break;

            case "entertainment":
                transactionHolder.tvIcon.setText("🎬");
                break;

            case "recharges":
                transactionHolder.tvIcon.setText("📱");
                break;

            default:
                transactionHolder.tvIcon.setText("💳");
                break;
        }

        // -------------------------
        // Description
        // -------------------------

        String description =
                transaction.getDescription();

        if (description == null ||
                description.trim().isEmpty()) {

            description = "No description";
        }

        transactionHolder.tvDescription
                .setText(description);

        // -------------------------
        // Amount
        // -------------------------

        String amount =
                String.format(
                        Locale.getDefault(),
                        "%.2f",
                        transaction.getAmount()
                );

        // -------------------------
        // Type
        // -------------------------

        String type =
                transaction.getType();

        if (type == null ||
                type.trim().isEmpty()) {

            type = "Expense";
        }

        transactionHolder.tvType.setText(type);

        if ("Income".equalsIgnoreCase(type)) {

            transactionHolder.tvAmount.setText(
                    "+ ₹ " + amount
            );

            transactionHolder.tvAmount.setTextColor(
                    Color.parseColor("#16A34A")
            );

            transactionHolder.tvType.setTextColor(
                    Color.parseColor("#16A34A")
            );

        } else {

            transactionHolder.tvAmount.setText(
                    "- ₹ " + amount
            );

            transactionHolder.tvAmount.setTextColor(
                    Color.parseColor("#DC2626")
            );

            transactionHolder.tvType.setTextColor(
                    Color.parseColor("#DC2626")
            );
        }

        // -------------------------
        // Date + Time
        // -------------------------

        String dateText =
                transaction.getTransactionDate();

        if (dateText != null) {

            try {

                dateText =
                        dateText.replace("T", " ");

                if (dateText.contains(".")) {

                    dateText =
                            dateText.substring(
                                    0,
                                    dateText.indexOf(".")
                            );
                }

                SimpleDateFormat inputFormat =
                        new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss",
                                Locale.getDefault()
                        );

                SimpleDateFormat outputFormat =
                        new SimpleDateFormat(
                                "dd MMM yyyy • hh:mm a",
                                Locale.getDefault()
                        );

                Date date =
                        inputFormat.parse(dateText);

                if (date != null) {

                    transactionHolder.tvDate.setText(
                            outputFormat.format(date)
                    );
                }

            } catch (Exception e) {

                transactionHolder.tvDate.setText(
                        dateText
                );
            }

        } else {

            transactionHolder.tvDate.setText(
                    "Date unavailable"
            );
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // =====================================================
    // DATE HEADER VIEW HOLDER
    // =====================================================

    public static class DateHeaderViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvDateHeader;

        public DateHeaderViewHolder(
                @NonNull View itemView) {

            super(itemView);

            tvDateHeader =
                    itemView.findViewById(
                            R.id.tvDateHeader
                    );
        }
    }

    // =====================================================
    // TRANSACTION VIEW HOLDER
    // =====================================================

    public static class TransactionViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvIcon;
        TextView tvCategory;
        TextView tvDescription;
        TextView tvAmount;
        TextView tvDate;
        TextView tvType;

        public TransactionViewHolder(
                @NonNull View itemView) {

            super(itemView);

            tvIcon =
                    itemView.findViewById(
                            R.id.tvIcon
                    );

            tvCategory =
                    itemView.findViewById(
                            R.id.tvCategory
                    );

            tvDescription =
                    itemView.findViewById(
                            R.id.tvDescription
                    );

            tvAmount =
                    itemView.findViewById(
                            R.id.tvAmount
                    );

            tvDate =
                    itemView.findViewById(
                            R.id.tvDate
                    );

            tvType =
                    itemView.findViewById(
                            R.id.tvType
                    );
        }
    }
}