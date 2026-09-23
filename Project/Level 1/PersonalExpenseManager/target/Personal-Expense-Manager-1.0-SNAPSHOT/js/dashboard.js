renderNav('dashboard');
(async function loadDashboard() {
    try {
        const data = await apiCall('dashboard');
        $('current-month').textContent = data.month;
        $('income-total').textContent = formatVnd(data.totals.income);
        $('expense-total').textContent = formatVnd(data.totals.expense);
        $('balance-total').textContent = formatVnd(data.totals.balance);
        $('category-summary').innerHTML = data.byCategory.length
            ? data.byCategory.map(row => `<div class="summary-row"><span>${esc(row.categoryName)} <small>${row.type === 'INCOME' ? 'Tiền vào' : 'Tiền ra'}</small></span><strong class="${row.type === 'INCOME' ? 'income-text' : 'expense-text'}">${formatVnd(row.total)}</strong></div>`).join('')
            : '<p class="muted">Chưa có giao dịch trong tháng này.</p>';
    } catch (error) { toast(error.message, 'error'); }
})();
