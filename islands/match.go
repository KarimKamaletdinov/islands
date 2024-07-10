package islands

import "github.com/hajimehoshi/ebiten/v2"

type Match struct {
	History []Map
	Geo     Geo
}

type Command struct {
	Method int8
	Vec    Vec
	Target *Unit
}

const (
	Move = iota
	Shoot
)

type Geo struct {
	Land []Rect
}

type Map struct {
	Geo   *Geo
	Units []Unit
}

func (m Map) Render(screen *ebiten.Image) {
	for _, unit := range m.Units {
		unit.Render(screen)
	}
}

type Unit struct {
	Vehicle     *Vehicle
	Position    Vec
	Damaged     bool
	SinceFire   int8
	Rotation    Vec
	GunRotation Vec
}

func (u Unit) Render(screen *ebiten.Image) {
	op := &ebiten.DrawImageOptions{}
	op.GeoM.Translate(float64(u.Position.X), float64(u.Position.Y))
	screen.DrawImage(u.Vehicle.Texture, op)
}

type Vehicle struct {
	Texture      *ebiten.Image
	Size         int16
	Speed        int8
	FireDistance int16
	FireDelay    int8
}

const (
	Tank = iota
	Gun
	Ship
)

var Vehicles = [...]Vehicle{
	Tank: {},
	Gun:  {},
	Ship: {},
}
