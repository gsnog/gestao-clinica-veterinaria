function DataTable({ columns, data, rowKey, emptyMessage = 'Nenhum registro encontrado.' }) {
  return (
    <section className="card">
      <table>
        <thead>
          <tr>
            {columns.map((column) => (
              <th key={column.key}>{column.header}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.length === 0 ? (
            <tr>
              <td colSpan={columns.length} className="table-empty">{emptyMessage}</td>
            </tr>
          ) : (
            data.map((item) => (
              <tr key={rowKey(item)}>
                {columns.map((column) => (
                  <td key={column.key} className={column.className}>{column.render(item)}</td>
                ))}
              </tr>
            ))
          )}
        </tbody>
      </table>
    </section>
  );
}

export default DataTable;
