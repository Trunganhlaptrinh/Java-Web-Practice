renderNav("transactions");
let categories = [],
  paymentMethods = [],
  transactions = [];

async function loadOptions() {
  categories = await apiCall("categories");
  paymentMethods = await apiCall("payment-methods");
  updateCategoryOptions();
  $("payment-method-id").innerHTML =
    '<option value="">Không áp dụng</option>' +
    paymentMethods
      .map((item) => `<option value="${item.id}">${esc(item.name)}</option>`)
      .join("");
}
function updateCategoryOptions() {
  const type = $("type").value;
  $("category-id").innerHTML =
    '<option value="">Chọn danh mục</option>' +
    categories
      .filter((item) => item.type === type)
      .map((item) => `<option value="${item.id}">${esc(item.name)}</option>`)
      .join("");
}
async function loadTransactions() {
  const params = new URLSearchParams();
  [
    ["search", "search"],
    ["type", "filter-type"],
    ["fromDate", "from-date"],
    ["toDate", "to-date"],
  ].forEach(([key, id]) => {
    if ($(id).value) params.set(key, $(id).value);
  });
  try {
    transactions = await apiCall("transactions?" + params);
    renderTransactions();
  } catch (error) {
    toast(error.message, "error");
  }
}
function renderTransactions() {
  $("transaction-rows").innerHTML = transactions.length
    ? transactions
        .map(
          (item) =>
            `<tr><td>${esc(item.transactionDate)}</td><td>${esc(item.description)}</td><td class="${item.type === "INCOME" ? "income-text" : "expense-text"}">${item.type === "INCOME" ? "Thu nhập" : "Chi tiêu"}</td><td>${esc(item.categoryName || "Không phân loại")}</td><td>${esc(item.paymentMethodName || "Không xác định")}</td><td class="${item.type === "INCOME" ? "income-text" : "expense-text"}">${item.type === "INCOME" ? "+" : "-"}${formatVnd(item.amount)}</td><td class="actions"><button class="btn small" data-edit="${item.id}">Sửa</button><button class="btn danger small" data-delete="${item.id}">Xóa</button></td></tr>`,
        )
        .join("")
    : '<tr><td colspan="7" class="muted">Chưa có giao dịch.</td></tr>';
}
function resetForm() {
  $("transaction-form").reset();
  $("transaction-id").value = "";
  $("transaction-date").value = today();
  $("form-title").textContent = "Thêm giao dịch";
  $("cancel-edit").hidden = true;
  updateCategoryOptions();
}
$("type").addEventListener("change", updateCategoryOptions);
$("filter-btn").addEventListener("click", loadTransactions);
$("cancel-edit").addEventListener("click", resetForm);
$("transaction-form").addEventListener("submit", async (event) => {
  event.preventDefault();
  const id = $("transaction-id").value;
  const body = {
    amount: Number($("amount").value),
    type: $("type").value,
    description: $("description").value,
    transactionDate: $("transaction-date").value,
    categoryId: Number($("category-id").value),
    paymentMethodId: $("payment-method-id").value
      ? Number($("payment-method-id").value)
      : null,
    note: $("note").value || null,
  };
  try {
    await apiCall(
      "transactions" + (id ? "" : ""),
      id ? "PUT" : "POST",
      id ? { ...body, id: Number(id) } : body,
    );
    toast(id ? "Đã cập nhật giao dịch" : "Đã thêm giao dịch", "success");
    resetForm();
    await loadTransactions();
  } catch (error) {
    toast(error.message, "error");
  }
});
$("transaction-rows").addEventListener("click", async (event) => {
  const editId = event.target.dataset.edit;
  const deleteId = event.target.dataset.delete;
  if (editId) {
    const item = transactions.find((row) => String(row.id) === editId);
    if (!item) return;
    $("transaction-id").value = item.id;
    $("amount").value = item.amount;
    $("type").value = item.type;
    updateCategoryOptions();
    $("category-id").value = item.categoryId || "";
    $("description").value = item.description;
    $("transaction-date").value = item.transactionDate;
    $("payment-method-id").value = item.paymentMethodId || "";
    $("note").value = item.note || "";
    $("form-title").textContent = "Sửa giao dịch";
    $("cancel-edit").hidden = false;
    window.scrollTo({ top: 0, behavior: "smooth" });
  }
  if (deleteId && confirm("Bạn có chắc muốn xóa giao dịch này?")) {
    try {
      await apiCall("transactions?id=" + deleteId, "DELETE");
      toast("Đã xóa giao dịch", "success");
      await loadTransactions();
    } catch (error) {
      toast(error.message, "error");
    }
  }
});
(async function init() {
  try {
    await loadOptions();
    resetForm();
    await loadTransactions();
  } catch (error) {
    toast(error.message, "error");
  }
})();
