function Topbar({ title, subtitle, action }) {
  return (
    <div className="topbar">
      <div>
        <div className="page-title">{title}</div>
        <div className="page-subtitle">{subtitle}</div>
      </div>
      {action || null}
    </div>
  );
}

export default Topbar;
