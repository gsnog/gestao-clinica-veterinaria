import { NavLink, Outlet } from "react-router-dom";
import useBodyClass from "../hooks/useBodyClass";
import { useAuth } from "../contexts/authContext";

const navItems = [
  { to: "/dashboard", icon: "🏠", label: "Dashboard", roles: ["VETERINARIO"] },
  { to: "/pets", icon: "🐶", label: "Pets", roles: ["TUTOR", "VETERINARIO"] },
  {
    to: "/consultas",
    icon: "📋",
    label: "Consultas",
    roles: ["TUTOR", "VETERINARIO"],
  },
  { to: "/tutores", icon: "👤", label: "Tutores", roles: ["VETERINARIO"] },
  {
    to: "/veterinarios",
    icon: "🩺",
    label: "Veterinários",
    roles: ["VETERINARIO"],
  },
  {
    to: "/perfil",
    icon: "🙍",
    label: "Perfil",
    roles: ["TUTOR", "VETERINARIO"],
  },
];

function MainLayout() {
  useBodyClass("");
  const { role, logout } = useAuth();

  const visibleItems = navItems.filter((item) => item.roles.includes(role));

  return (
      <>
        <aside className="sidebar">
          <div className="brand">
            <div className="brand-icon">
              <img src={`${import.meta.env.BASE_URL}images/logo.png`} alt="logo" />
            </div>
            <span className="brand-name">VetCare</span>
          </div>

          <span className="nav-section">Principal</span>

          {visibleItems.map((item) => (
              <NavLink
                  key={item.to}
                  to={item.to}
                  className={({ isActive }) => `nav-item ${isActive ? "active" : ""}`}
              >
                <span className="nav-icon">{item.icon}</span>
                {item.label}
              </NavLink>
          ))}

          <NavLink to="/login" className="nav-item" onClick={logout}>
            <span className="nav-icon">↩</span>
            Sair
          </NavLink>

          <div className="sidebar-footer">
            <div className="sidebar-footer-text">
              VetCare v1.0
              <br />
              Gestão Veterinária – UFF
            </div>
          </div>
        </aside>

        <Outlet />
      </>
  );
}

export default MainLayout;