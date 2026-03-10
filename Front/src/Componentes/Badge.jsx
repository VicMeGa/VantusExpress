
const badgeClass = {
  "registrado":   "badge badge--registrado",
  "en tránsito":  "badge badge--en-transito",
  "entregado":    "badge badge--entregado",
  "cancelado":    "badge badge--cancelado",
};

function Badge({ estado }) {
  return <span className={badgeClass[estado] || "badge"}>{estado}</span>;
}

export default Badge;